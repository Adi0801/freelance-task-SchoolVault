CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    issued_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    due_date DATE NOT NULL,
    returned_at TIMESTAMP NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ISSUED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_transactions_book FOREIGN KEY (book_id) REFERENCES books(id),
    CONSTRAINT fk_transactions_member FOREIGN KEY (member_id) REFERENCES members(id),
    INDEX idx_transactions_book_id (book_id),
    INDEX idx_transactions_member_id (member_id),
    INDEX idx_transactions_status (status),
    INDEX idx_transactions_book_member_status (book_id, member_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
