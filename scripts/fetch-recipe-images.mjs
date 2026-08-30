#!/usr/bin/env node
// FR-114 (2026-08-30, requested: "zamiast ikonek chce prawdziwe zdjecia
// dan, wyszukaj i pobierz obrazek z internetu dla kazdego dania"): batch,
// OFFLINE fetch of one Unsplash photo per recipe -- run ONCE by a human
// with an Unsplash Access Key, not called live from the app (a client-side
// PWA/mobile app has no shared cache to hold a per-recipe lookup, and
// hitting Unsplash's API on every page load for 213 recipes would blow
// through the rate limit in seconds for every single user).
//
// Usage:
//   UNSPLASH_ACCESS_KEY=xxxxx node scripts/fetch-recipe-images.mjs
//
// Resumable: skips any recipe that already has an "image" field, so a run
// interrupted by Unsplash's rate limit (50 req/h on a "Demo" application,
// the default until you request production access) can just be re-run
// later -- it picks up where it left off. Writes progress back to both
// index.html and android/app/src/main/assets/recipes.json after EVERY
// recipe, not just at the end, so a Ctrl+C or a rate-limit stop never
// loses already-fetched images.
//
// Search query strategy: Unsplash's index is primarily English, and this
// app's recipe names are Polish home-cooking names ("Owsianka na mleku ze
// skyrem, rodzynkami, miodem i orzechami") that mostly don't exist as
// stock-photo captions verbatim. TRANSLATIONS below maps common Polish
// food words found in the name/ingredients to English search terms; the
// first 1-2 matched words (plus "food" as a generic anchor) become the
// query. This is a best-effort heuristic, not a guarantee of a good
// match per recipe -- expect to spot-check results and may want to
// manually override a handful of misses (see OVERRIDES below).

import fs from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const ROOT = path.resolve(__dirname, "..");
const INDEX_HTML = path.join(ROOT, "index.html");
const RECIPES_JSON = path.join(ROOT, "android", "app", "src", "main", "assets", "recipes.json");

const ACCESS_KEY = process.env.UNSPLASH_ACCESS_KEY;
if (!ACCESS_KEY) {
  console.error("Set UNSPLASH_ACCESS_KEY first, e.g.:\n  UNSPLASH_ACCESS_KEY=xxxxx node scripts/fetch-recipe-images.mjs");
  process.exit(1);
}

// Polish food word -> English search term. Longest-word-first matching so
// e.g. "owsianka" doesn't get shadowed by a shorter partial hit.
const TRANSLATIONS = {
  "jajecznica": "scrambled eggs", "jajko": "eggs", "jajka": "eggs", "omlet": "omelette",
  "owsianka": "oatmeal", "owsiane": "oats", "musli": "muesli", "granola": "granola",
  "kanapka": "sandwich", "kanapki": "sandwiches", "tost": "toast", "tosty": "toast",
  "kurczak": "chicken", "indyk": "turkey", "wołowina": "beef", "wołowy": "beef",
  "wieprzowina": "pork", "schab": "pork chop", "łosoś": "salmon", "dorsz": "cod",
  "tuńczyk": "tuna", "krewetki": "shrimp", "ryba": "fish",
  "sałatka": "salad", "sałatką": "salad", "zupa": "soup", "zupę": "soup", "krem": "cream soup",
  "makaron": "pasta", "ryż": "rice", "kasza": "grain bowl", "quinoa": "quinoa",
  "ziemniaki": "potatoes", "ziemniaki": "potatoes", "frytki": "fries",
  "warzywa": "vegetables", "warzywami": "vegetables", "warzywny": "vegetables",
  "owoce": "fruit", "owocowy": "fruit", "jogurt": "yogurt", "skyr": "skyr yogurt",
  "twaróg": "cottage cheese", "ser": "cheese", "serem": "cheese", "feta": "feta cheese",
  "hummus": "hummus", "ciecierzyca": "chickpeas", "soczewica": "lentils", "fasola": "beans",
  "tofu": "tofu", "shakshuka": "shakshuka", "szakszuka": "shakshuka",
  "smoothie": "smoothie", "koktajl": "smoothie", "shake": "protein shake",
  "placki": "pancakes", "naleśniki": "crepes", "gofry": "waffles",
  "chleb": "bread", "bułka": "bun", "bagietka": "baguette",
  "curry": "curry", "gulasz": "stew", "chili": "chili con carne",
  "burger": "burger", "wrap": "wrap sandwich", "tortilla": "tortilla wrap",
  "pieczony": "roasted", "grillowany": "grilled", "duszony": "braised",
  "awokado": "avocado", "szpinak": "spinach", "brokuł": "broccoli", "brokuły": "broccoli",
  "pomidor": "tomato", "pomidory": "tomatoes", "ogórek": "cucumber",
  "orzechy": "nuts", "migdały": "almonds", "chia": "chia seeds", "siemię": "flax seeds",
  "batonik": "energy bar", "muffinka": "muffin", "ciasto": "cake", "deser": "dessert",
  "budyń": "pudding", "kisiel": "fruit pudding", "galaretka": "jelly dessert",
};

const CAT_FALLBACK = {
  sniadania: "breakfast", drugie: "healthy snack", obiady: "dinner plate",
  kolacje: "dinner plate", deser: "dessert",
};

// Requested fix (2026-08-30, found on the FIRST live run before it could
// waste more rate-limited requests): "pomidorki koktajlowe" (cherry
// tomatoes) matched S2 to a "smoothie" photo, because the original
// haystack.includes() substring check found "koktajl" INSIDE
// "koktajlowe" -- an ADJECTIVE meaning "cherry-sized", sharing a stem
// with but otherwise unrelated to "koktajl" the smoothie noun. Same class
// of bug would also strike "ser" (cheese) matching inside "deser"
// (dessert). Fixed by tokenizing and matching against whole tokens
// (token.startsWith(key), not haystack.includes(key)) -- still allows the
// prefix matches the dictionary relies on for Polish inflection
// (kurczak/kurczaka/kurczakiem all still hit "kurczak"), just no longer
// matches mid-word. koktajl itself gets an extra, explicit allow-list of
// its own noun forms on top of that, since "koktajlowy/-a/-e/-ych" would
// still token.startsWith("koktajl") otherwise.
function tokenize(text) {
  return text.toLowerCase().match(/\p{L}+/gu) || [];
}
const KOKTAJL_NOUN_FORMS = new Set(["koktajl", "koktajlu", "koktajlem", "koktajle", "koktajli", "koktajlom", "koktajlach", "koktajlami"]);

// Manual overrides for recipe IDs where the heuristic query is known to be
// wrong -- add an entry here (and re-run) after spot-checking results,
// rather than fighting the general dictionary for one-off cases.
const OVERRIDES = {};

function buildQuery(recipe) {
  if (OVERRIDES[recipe.id]) return OVERRIDES[recipe.id];
  const tokens = tokenize(recipe.name + " " + recipe.ingredients.join(" "));
  const keys = Object.keys(TRANSLATIONS).sort((a, b) => b.length - a.length);
  const hits = [];
  for (const token of tokens) {
    for (const key of keys) {
      const matches = key === "koktajl" ? KOKTAJL_NOUN_FORMS.has(token) : token.startsWith(key);
      if (matches && !hits.includes(TRANSLATIONS[key])) {
        hits.push(TRANSLATIONS[key]);
        break;
      }
    }
    if (hits.length >= 2) break;
  }
  if (hits.length === 0) hits.push(CAT_FALLBACK[recipe.cat] || "food");
  return hits.join(" ") + " food";
}

async function searchPhoto(query) {
  const url = `https://api.unsplash.com/search/photos?query=${encodeURIComponent(query)}&per_page=1&orientation=squarish&content_filter=high`;
  const res = await fetch(url, { headers: { Authorization: `Client-ID ${ACCESS_KEY}` } });
  if (res.status === 403) {
    const body = await res.text();
    throw Object.assign(new Error(`Rate limited (403): ${body.slice(0, 200)}`), { rateLimited: true });
  }
  if (!res.ok) throw new Error(`Unsplash ${res.status}: ${await res.text()}`);
  const data = await res.json();
  const photo = data.results && data.results[0];
  if (!photo) return null;
  // Unsplash API guideline: trigger the download endpoint when a photo is
  // actually used (not just previewed in search results).
  fetch(`${photo.links.download_location}&client_id=${ACCESS_KEY}`).catch(() => {});
  return {
    image: photo.urls.small,
    imageCredit: {
      name: photo.user.name,
      profileUrl: photo.user.links.html,
    },
  };
}

function extractRecipesFromHtml(html) {
  const marker = "const RECIPES = ";
  const start = html.indexOf(marker);
  if (start === -1) throw new Error("const RECIPES = [ not found in index.html");
  const arrayStart = start + marker.length;
  // Find the matching closing bracket for the array literal.
  let depth = 0, inString = false, escape = false, end = -1;
  for (let i = arrayStart; i < html.length; i++) {
    const ch = html[i];
    if (inString) {
      if (escape) escape = false;
      else if (ch === "\\") escape = true;
      else if (ch === '"') inString = false;
      continue;
    }
    if (ch === '"') inString = true;
    else if (ch === "[") depth++;
    else if (ch === "]") { depth--; if (depth === 0) { end = i + 1; break; } }
  }
  if (end === -1) throw new Error("Could not find end of RECIPES array");
  return { arrayText: html.slice(arrayStart, end), start: arrayStart, end };
}

async function main() {
  const html = fs.readFileSync(INDEX_HTML, "utf8");
  const { arrayText, start, end } = extractRecipesFromHtml(html);
  const recipes = JSON.parse(arrayText);

  const todo = recipes.filter((r) => !r.image);
  console.log(`${recipes.length} recipes total, ${todo.length} still need an image.`);

  let fetched = 0;
  for (const recipe of todo) {
    const query = buildQuery(recipe);
    process.stdout.write(`[${recipe.id}] "${recipe.name}" -> query "${query}" ... `);
    let result;
    try {
      result = await searchPhoto(query);
    } catch (err) {
      if (err.rateLimited) {
        console.log("\nRate limited by Unsplash -- stopping here. Re-run this script later to resume.");
        break;
      }
      console.log(`ERROR: ${err.message}`);
      continue;
    }
    if (!result) {
      console.log("no result found, skipping (leaves emoji fallback).");
      continue;
    }
    recipe.image = result.image;
    recipe.imageCredit = result.imageCredit;
    console.log(`OK (${result.imageCredit.name})`);
    fetched++;

    // Write progress after every recipe so an interruption never loses work.
    const newHtml = html.slice(0, start) + JSON.stringify(recipes) + html.slice(end);
    fs.writeFileSync(INDEX_HTML, newHtml);
    fs.writeFileSync(RECIPES_JSON, JSON.stringify(recipes, null, 0));

    // Unsplash Demo tier: 50 req/h = one request per 72s to never trip the
    // limit; this pacing plus the resumable design means a full 213-recipe
    // run realistically takes multiple sessions unless you have production
    // access (5000 req/h) approved for your Unsplash application.
    await new Promise((r) => setTimeout(r, 73000));
  }

  console.log(`\nDone this run: ${fetched} image(s) fetched. ${todo.length - fetched} still missing (re-run to continue, or check the "no result" ones manually).`);
}

main().catch((err) => {
  console.error(err);
  process.exit(1);
});
