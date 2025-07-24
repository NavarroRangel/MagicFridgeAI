
CREATE TABLE food_item (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nome VARCHAR(255) NOT NULL,
                           categoria VARCHAR(100) NOT NULL,
                           quantidade INT NOT NULL,
                           validade DATE NOT NULL
);