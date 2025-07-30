INSERT INTO product (name, price, image_url)
VALUES ('맛있는 케이크', 25000, 'https://example.com/cake.jpg');

INSERT INTO member (email, password)
VALUES ('tjdrj530@naver.com', 'tjdrj530');

INSERT INTO wish_list (user_id, product_id, quantity)
VALUES (1, 1, 4);

INSERT INTO option (name, quantity, product_id)
VALUES ('딸기 케이크', 100, 1),
       ('초콜릿 케이크', 15, 1);
