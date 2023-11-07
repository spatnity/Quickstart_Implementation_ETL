CREATE TABLE IF NOT EXISTS Source (id SERIAL PRIMARY KEY, hotel_name VARCHAR(255), price DECIMAL, review VARCHAR(255));

INSERT INTO Source (id, hotel_name, price, review) VALUES
(1, 'Grand', 100, 'best'),
(2, 'Middle', 20, 'good'),
(3, 'Small', 17, 'worst');

