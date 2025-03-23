-- host
INSERT INTO host (id, username, bank, account, contact_from, contact_until, created_at, modified_at)
VALUES (1, '호스트', 'KB국민', '00000000000000', '09:00:00', '18:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- tag
INSERT INTO tag
VALUES (1, '따뜻한');

INSERT INTO tag
VALUES (2, '세련된');

INSERT INTO tag
VALUES (3, '재즈');

INSERT INTO tag
VALUES (4, '악기 가이드');

INSERT INTO tag
VALUES (5, '밴드 소개');

-- place
INSERT INTO address(id, zipcode, state, city, street_address, detail, latitude, longitude)
VALUES (1, '00000', '경기도', '수원시 장안구', '율전로', '상세주소', 000.0000000000, 000.0000000000);

INSERT INTO place (id, host_id, brn, address_id, title, content, booking_from, booking_until, parking_capacity, parking_location, free_parking, created_at, modified_at)
VALUES (1, 1, '0000000000', 1, '플레이스', '내용', 2592000000000000, 604800000000000, 2, '건물 뒤편', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO link(id, links_id, url)
VALUES (1, 1, 'https://www.ontherec.kr');

INSERT INTO holiday(id, holidays_id, type)
VALUES (1, 1, '설날');

INSERT INTO place_tags(place_id, tags_id)
VALUES (1, 1);

INSERT INTO place_tags(place_id, tags_id)
VALUES (1, 2);

INSERT INTO place_tags(place_id, tags_id)
VALUES (1, 3);

-- stage
INSERT INTO stage (id, place_id, title, content, guide, view_count, like_count, floor, has_elevator, min_capacity, max_capacity, stage_type, stage_width, stage_height, stage_managing_available, stage_managing_fee, sound_engineering_available, sound_engineering_fee, light_engineering_available, light_engineering_fee, photographing_available, photographing_fee, application_form, cue_sheet_template, cue_sheet_due, has_restroom, has_wifi, has_camera_standing, has_waiting_room, has_projector, has_locker, allows_water, allows_drink, allows_food, allows_food_delivery, allows_alcohol, sell_drink, sell_alcohol, created_at, modified_at)
VALUES (1, 1, '스테이지', '내용', '가이드', 10, 3, -1, false, 60, 120, 'RECTANGLE', 8, 5, true, 0, true, 50000, false, null, true, 50000, 'https://docs.google.com/document', 'https://docs.google.com/document', 259200000000000, true, true, true, false, true, false, true, false, false, false, false, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO refund_policy(id, refund_policies_id, day_before, percent)
VALUES (1, 1, 7, 100);

INSERT INTO stage_tags(stage_id, tags_id)
VALUES (1, 1);

INSERT INTO stage_tags(stage_id, tags_id)
VALUES (1, 2);

INSERT INTO stage_tags(stage_id, tags_id)
VALUES (1, 3);

-- post
INSERT INTO post (id, author, title, content, view_count, like_count, created_at, modified_at)
VALUES (1, '호스트', '아티클', '내용', 10, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO post_tags(post_id, tags_id)
VALUES (1, 4);

INSERT INTO post_tags(post_id, tags_id)
VALUES (1, 5);
