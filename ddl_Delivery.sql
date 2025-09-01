CREATE TABLE deliveries
(
    delivery_id      BIGINT       NOT NULL,
    pickup_address   TEXT         NULL,
    delivery_address TEXT         NULL,
    status           VARCHAR(255) NULL,
    pickup_time      datetime     NULL,
    delivery_time    datetime     NULL,
    tracking_number  VARCHAR(255) NULL,
    hub_id           BIGINT       NULL,
    agent_id         BIGINT       NULL,
    transaction_id   BIGINT       NULL,
    created_at       datetime     NULL,
    CONSTRAINT pk_deliveries PRIMARY KEY (delivery_id)
);