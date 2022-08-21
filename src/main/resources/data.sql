INSERT INTO RECIPES(ID, NAME, TYPE, SERVING_CAPACITY, INSTRUCTIONS)
VALUES (101, 'DISH1', 'VEG', 3, 'Oven');
INSERT INTO RECIPES(ID, NAME, TYPE, SERVING_CAPACITY, INSTRUCTIONS)
VALUES (102, 'DISH2', 'VEG', 3, 'normal');
INSERT INTO RECIPES(ID, NAME, TYPE, SERVING_CAPACITY, INSTRUCTIONS)
VALUES (103, 'DISH3', 'VEG', 3, 'Oven');
INSERT INTO RECIPES(ID, NAME, TYPE, SERVING_CAPACITY, INSTRUCTIONS)
VALUES (104, 'DISH4', 'NON-VEG', 3, 'Oven');

INSERT INTO RECIPE_ENTITY_INGREDIENTS(RECIPE_ENTITY_ID, NAME) VALUES (101, 'potato');
INSERT INTO RECIPE_ENTITY_INGREDIENTS(RECIPE_ENTITY_ID, NAME) VALUES (102, 'tomato');
INSERT INTO RECIPE_ENTITY_INGREDIENTS(RECIPE_ENTITY_ID, NAME) VALUES (103, 'potato');
INSERT INTO RECIPE_ENTITY_INGREDIENTS(RECIPE_ENTITY_ID, NAME) VALUES (103, 'tomato');
INSERT INTO RECIPE_ENTITY_INGREDIENTS(RECIPE_ENTITY_ID, NAME) VALUES (104, 'chicken');