CREATE TABLE hotels (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    rating INT,
    total_rooms INT NOT NULL,
    weekday_regular DOUBLE NOT NULL,
    weekend_regular DOUBLE NOT NULL,
    weekday_reward DOUBLE NOT NULL,
    weekend_reward DOUBLE NOT NULL
);

CREATE TABLE bookings(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hotel_id BIGINT,
    check_in_date DATE,
    check_out_date DATE,
    CONSTRAINT fk_hotel FOREIGN KEY (hotel_id) REFERENCES hotels(id)
);