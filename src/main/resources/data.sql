-- host
INSERT INTO host (id, username, bank, account, contact_from, contact_until, created_at, modified_at)
VALUES (1, '호스트', 'KB국민', '00000000000000', '09:00:00', '18:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- stage
INSERT INTO address (id, zipcode, state, city, street_address, detail, latitude, longitude)
VALUES (1, '00000', '경기도', '수원시 장안구', '율전로', '상세주소', 000.0000000000, 000.0000000000);

INSERT INTO stage (id, host_id, title, brn, address_id, view_count, like_count, content, min_capacity, max_capacity, stage_type, stage_width, stage_height, booking_from, booking_until, stage_managing_available, stage_managing_fee, sound_engineering_available, sound_engineering_fee, light_engineering_available, light_engineering_fee, photographing_available, photographing_fee, application_form, cue_sheet_template, cue_sheet_due, parking_capacity, parking_location, free_parking, has_elevator, has_restroom, has_wifi, has_camera_standing, has_waiting_room, has_projector, has_locker, allows_water, allows_drink, allows_food, allows_food_delivery, allows_alcohol, sell_drink, sell_alcohol, created_at, modified_at)
VALUES (1, 1, '스테이지1', '0000000001', 1, 10, 3, '내용', 60, 120, 'RECTANGLE', 8, 5, 2592000000000000, 604800000000000, true, 0, true, 50000, false, null, true, 50000, 'https://docs.google.com/document', 'https://docs.google.com/document', 259200000000000, 2, '건물 뒤편', true, false, true, true, true, false, true, false, true, false, false, false, false, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO stage_images (stage_id, images)
VALUES (1, 'https://d3j0mzt56d6iv2.cloudfront.net/images/o/test/logo-symbol.jpg');

INSERT INTO stage_tags (stage_id, tags)
VALUES (1, '따뜻한'),
       (1, '세련된'),
       (1, '재즈');

INSERT INTO stage_links (stage_id, links)
VALUES (1, 'https://www.ontherec.kr');

INSERT INTO stage_holidays (stage_id, holidays)
VALUES (1, '설날');

INSERT INTO refund_policy (id, refund_policies_id, day_before, percent)
VALUES (1, 1, 7, 100);

INSERT INTO address (id, zipcode, state, city, street_address, detail, latitude, longitude)
VALUES (2, '00000', '경기도', '수원시 장안구', '율전로', '상세주소', 000.0000000000, 000.0000000000);

INSERT INTO stage (id, host_id, title, brn, address_id, view_count, like_count, content, min_capacity, max_capacity, stage_type, stage_width, stage_height, booking_from, booking_until, stage_managing_available, stage_managing_fee, sound_engineering_available, sound_engineering_fee, light_engineering_available, light_engineering_fee, photographing_available, photographing_fee, application_form, cue_sheet_template, cue_sheet_due, parking_capacity, parking_location, free_parking, has_elevator, has_restroom, has_wifi, has_camera_standing, has_waiting_room, has_projector, has_locker, allows_water, allows_drink, allows_food, allows_food_delivery, allows_alcohol, sell_drink, sell_alcohol, created_at, modified_at)
VALUES (2, 1, '스테이지2', '0000000002', 2, 10, 3, '내용', 60, 120, 'RECTANGLE', 8, 5, 2592000000000000, 604800000000000, true, 0, true, 50000, false, null, true, 50000, 'https://docs.google.com/document', 'https://docs.google.com/document', 259200000000000, 2, '건물 뒤편', true, false, true, true, true, false, true, false, true, false, false, false, false, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO stage_images (stage_id, images)
VALUES (2, 'https://d3j0mzt56d6iv2.cloudfront.net/images/o/test/logo-symbol.jpg');

INSERT INTO stage_tags (stage_id, tags)
VALUES (2, '따뜻한'),
       (2, '세련된'),
       (2, '재즈');

INSERT INTO stage_links (stage_id, links)
VALUES (2, 'https://www.ontherec.kr');

INSERT INTO stage_holidays (stage_id, holidays)
VALUES (2, '설날');

INSERT INTO refund_policy (id, refund_policies_id, day_before, percent)
VALUES (2, 2, 7, 100);

-- post
INSERT INTO post (id, author, title, content, view_count, like_count, created_at, modified_at)
VALUES (1, 'test', '아티클', '내용', 10, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO post_images (post_id, images)
VALUES (1, 'https://d3j0mzt56d6iv2.cloudfront.net/images/o/test/logo-symbol.jpg');

-- chat
INSERT INTO chat (id, title)
VALUES (1, '채팅방');

INSERT INTO participant (id, participants_id, username, read_at)
VALUES (1, 1, 'test', CURRENT_TIMESTAMP),
       (2, 1, '호스트', CURRENT_TIMESTAMP);

INSERT INTO message (id, chat_id, type, username, content, created_at, modified_at)
VALUES (1, 1, 'NOTICE', 'SYSTEM', '채팅방이 생성되었습니다', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 1, 'TEXT', 'test', '메시지1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 1, 'TEXT', '호스트', '메시지2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (4, 1, 'TEXT', 'test', '메시지3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (5, 1, 'TEXT', '호스트', '메시지4', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (6, 1, 'TEXT', 'test', '메시지5', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (7, 1, 'TEXT', '호스트', '메시지6', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (8, 1, 'TEXT', 'test', '메시지7', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (9, 1, 'TEXT', '호스트', '메시지8', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (10, 1, 'TEXT', 'test', '메시지9', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (11, 1, 'TEXT', '호스트', '메시지10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
