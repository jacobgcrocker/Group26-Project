# Recipe Search Query

Edamam Recipe Search API Docs: https://developer.edamam.com/edamam-docs-recipe-api

## Must Include

- `app_id`: string
- `app_key`: string

## Main Criteria:

- `ingr`: string  
  Filter by number of ingredients (MIN+, MIN-MAX, MAX), where MIN and MAX are integer numbers.  
  Example: `ingr=5-8`

- `diet`: array[string]  
  Diet label. You can select multiple labels.  
  Available values: balanced, high-fiber, high-protein, low-carb, low-fat, low-sodium

- `health`: array[string]  
  Health label. You can select multiple labels.  
  Available values: alcohol-cocktail, alcohol-free, celery-free, crustacean-free, dairy-free, DASH, egg-free, fish-free, fodmap-free, gluten-free, immuno-supportive, keto-friendly, kidney-friendly, kosher, low-fat-abs, low-potassium, low-sugar, lupine-free, Mediterranean, mollusk-free, mustard-free, no-oil-added, paleo, peanut-free, pescatarian, pork-free, red-meat-free, sesame-free, shellfish-free, soy-free, sugar-conscious, sulfite-free, tree-nut-free, vegan, vegetarian, wheat-free

- `mealType`: array[string]  
  The type of meal a recipe belongs to.  
  Available values: Breakfast, Dinner, Lunch, Snack, Teatime

- `calories`: string  
  Filter recipes by calorie range.  
  The format is calories=RANGE where RANGE is in one of MIN+, MIN-MAX or MAX, where MIN and MAX are non-negative floating point numbers.  
  Examples:

  - “calories=100-300” will return all recipes with between 100 and 300 kcal per serving.
  - Note: we can implement this as preset ranges:
    - Low: Below 300
    - Moderate: 300-1000
    - High: Over 1000

- `time`: string  
  Time range for the total cooking and prep time for a recipe.  
  The format is time=RANGE where RANGE is one of MIN+, MIN-MAX or MAX, where MIN and MAX are non-negative integer numbers.  
  Examples:

  - “time=1%2B” will return all recipes with available total time greater than 1 minute

- `random`: boolean  
  Select whether you want this query to respond with a random selection of 20 recipes based on the criteria filled. If there were only 20 or fewer possible results, this will return those results in random order.

  - Note: we probably want to use this to limit the number of results to speed up response time

- `field`: array[string]  
  Recipe fields to be included in the response.  
  Available values: uri, label, image, images, source, url, shareAs, yield, dietLabels, healthLabels, cautions, ingredientLines, ingredients, calories, glycemicIndex, inflammatoryIndex, totalCO2Emissions, co2EmissionsClass, totalWeight, totalTime, cuisineType, mealType, dishType, totalNutrients, totalDaily, digest, tags, externalId
  - Note: we may want to use this if there are specific info we want to know about in the recipe

## Weak Criteria, May Not Include These:

- `cuisineType`: array[string]  
  The type of cuisine of the recipe.  
  Available values: American, Asian, British, Caribbean, Central Europe, Chinese, Eastern Europe, French, Indian, Italian, Japanese, Kosher, Mediterranean, Mexican, Middle Eastern, Nordic, South American, South East Asian

- `dishType`: array[string]  
  The dish type a recipe belongs to.  
  Available values: Biscuits and cookies, Bread, Cereals, Condiments and sauces, Desserts, Drinks, Main course, Pancake, Preps, Preserve, Salad, Sandwiches, Side dish, Soup, Starter, Sweets

- `imageSize`: array[string]  
  Show only recipes which have images with selected sizes.  
  Available values: LARGE, REGULAR, SMALL, THUMBNAIL

- `excluded`: array[string]  
  Exclude recipes with certain ingredients.  
  The format is excluded=FOOD where FOOD is replaced by the name of the specific food you don’t want to be present in the recipe results. More than one food can be excluded at the same time.  
  Example: excluded=vinegar&excluded=pretzel will exclude any recipes which contain vinegar or pretzels in their ingredient list

- `nutrients[NAME]`: string  
  Specify nutrient ranges.  
  For example: nutrients[CA]=50+ means minimum 50mg calcium, where ‘50+’ has to be properly encoded as ‘50%2B’ nutrients[FAT]=30 means maximum 30g fat and nutrients[FE]=5-10 means iron between 5mg and 10mg inclusive

- `co2EmissionsClass`: string  
  Filter recipes by their CO2 footprint.  
  Available values : A+, A, B, C, D, E, F, G

- `tag`: array[string]  
  Only show recipes with the specified tags.  
  Example values: FAT, SUGAR, PROCNT, CHOLE, NA
