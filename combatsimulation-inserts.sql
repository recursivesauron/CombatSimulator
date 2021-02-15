INSERT INTO moves (id, type, name, value, mana_cost, rage_cost) values (default, 'DAMAGE', 'Tackle', 10, 0, 0);
INSERT INTO moves (id, type, name, value, mana_cost, rage_cost)  values (default, 'DAMAGE', 'Pound', 15, 0, 0);
INSERT INTO moves (id, type, name, value, mana_cost, rage_cost)  values (default, 'DAMAGE', 'Quick Attack', 60, 0, 0);
INSERT INTO moves (id, type, name, value, mana_cost, rage_cost)  values (default, 'DAMAGE', 'Swipe', 30, 0, 0);
INSERT INTO moves (id, type, name, value, mana_cost, rage_cost)  values (default, 'DAMAGE', 'Thrash', 55, 0, 0);
INSERT INTO moves (id, type, name, value, mana_cost, rage_cost)  values (default, 'HEALING', 'Flash Heal', 40, 20, 0);
INSERT INTO moves (id, type, name, value, mana_cost, rage_cost)  values (default, 'HEALING', 'Holy Shock', 100, 50, 0);
INSERT INTO moves (id, type, name, value, mana_cost, rage_cost)  values (default, 'HEALING', 'Swiftmend', 80, 40, 0);
INSERT INTO moves (id, type, name, value, mana_cost, rage_cost)  values (default, 'RAGE', 'Body Slam', 100, 0, 50);
INSERT INTO moves (id, type, name, value, mana_cost, rage_cost)  values (default, 'RAGE', 'March of the Murlocs', 300, 0, 100);

INSERT INTO characters (id, name, class, ally, hp_limit, mana_limit, rage_limit) values (default, 'Sevrielle', 'Paladin', 1, 800, 400, 0);
INSERT INTO characters (id, name, class, ally, hp_limit, mana_limit, rage_limit) values (default, 'Ursoc', 'Druid', 1, 1000, 300, 0);
INSERT INTO characters (id, name, class, ally, hp_limit, mana_limit, rage_limit) values (default, 'Murloc 1', 'Murloc', 0, 800, 0, 500);
INSERT INTO characters (id, name, class, ally, hp_limit, mana_limit, rage_limit) values (default, 'Murloc 2', 'Murloc', 0, 800, 0, 500);

INSERT INTO enemy_sets (source_id, target_id) values (1, 3);
INSERT INTO enemy_sets (source_id, target_id) values (1, 4);
INSERT INTO enemy_sets (source_id, target_id) values (2, 3);
INSERT INTO enemy_sets (source_id, target_id) values (2, 4);
INSERT INTO enemy_sets (source_id, target_id) values (3, 1);
INSERT INTO enemy_sets (source_id, target_id) values (3, 2);
INSERT INTO enemy_sets (source_id, target_id) values (4, 1);
INSERT INTO enemy_sets (source_id, target_id) values (4, 2);

INSERT INTO ally_sets (source_id, ally_id) values (1, 2);
INSERT INTO ally_sets (source_id, ally_id) values (2, 1);

INSERT INTO move_sets (character_id, move_id)  values (1, 1);
INSERT INTO move_sets (character_id, move_id)  values (1, 2);
INSERT INTO move_sets (character_id, move_id)  values (1, 3);
INSERT INTO move_sets (character_id, move_id)  values (1, 6);
INSERT INTO move_sets (character_id, move_id)  values (1, 7);
INSERT INTO move_sets (character_id, move_id)  values (2, 4);
INSERT INTO move_sets (character_id, move_id)  values (2, 5);
INSERT INTO move_sets (character_id, move_id)  values (2, 8);
INSERT INTO move_sets (character_id, move_id)  values (3, 1);
INSERT INTO move_sets (character_id, move_id)  values (3, 2);
INSERT INTO move_sets (character_id, move_id)  values (3, 9);
INSERT INTO move_sets (character_id, move_id)  values (3, 10);
INSERT INTO move_sets (character_id, move_id)  values (4, 1);
INSERT INTO move_sets (character_id, move_id)  values (4, 2);
INSERT INTO move_sets (character_id, move_id)  values (4, 9);
INSERT INTO move_sets (character_id, move_id)  values (4, 10);