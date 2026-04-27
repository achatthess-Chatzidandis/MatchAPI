INSERT INTO matches (description, match_date, match_time, team_a, team_b, sport)
VALUES ('OSFP-PAO', '2021-03-31', '12:00', 'OSFP', 'PAO', 1);
INSERT INTO matches (description, match_date, match_time, team_a, team_b, sport)
VALUES ('PAOK-AEK', '2021-04-01', '16:00', 'PAOK', 'AEK', 1);
INSERT INTO matches (description, match_date, match_time, team_a, team_b, sport)
VALUES ('PAOK-ARIS', '2025-05-15', '18:00', 'PAOK', 'ARIS', 2);

INSERT INTO match_odds (match_id, specifier, odd)
VALUES (1, '1', '1.3');
INSERT INTO match_odds (match_id, specifier, odd)
VALUES (1, 'X', '2.5');
INSERT INTO match_odds (match_id, specifier, odd)
VALUES (1, '2', '3.5');

INSERT INTO match_odds (match_id, specifier, odd)
VALUES (2, '1', '1.2');
INSERT INTO match_odds (match_id, specifier, odd)
VALUES (2, 'X', '2.3');
INSERT INTO match_odds (match_id, specifier, odd)
VALUES (2, '2', '3.4');

INSERT INTO match_odds (match_id, specifier, odd)
VALUES (3, '1', '1.8');
INSERT INTO match_odds (match_id, specifier, odd)
VALUES (3, '2', '2.9');