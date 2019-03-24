DROP TABLE IF EXISTS instruments;
CREATE TABLE instruments (
  id BIGSERIAL NOT NULL,
  name VARCHAR(255) NOT NULL,
  precision SMALLINT NOT NULL,

  x_created_on TIMESTAMP NOT NULL,
  x_updated_on TIMESTAMP,
  x_deleted_on TIMESTAMP,

  CONSTRAINT instruments_pk_id PRIMARY KEY(id)
);

CREATE UNIQUE INDEX instruments_uq_name ON instruments (name);


DROP TABLE IF EXISTS strategies;
CREATE TABLE strategies (
  id BIGSERIAL NOT NULL,
  name VARCHAR(255) NOT NULL,
  class VARCHAR(255) NOT NULL,
  checksum VARCHAR(255) NOT NULL,
  active BOOLEAN NOT NULL,
  status VARCHAR(16) NOT NULL,

  x_created_on TIMESTAMP NOT NULL,
  x_updated_on TIMESTAMP,
  x_deleted_on TIMESTAMP,

  CONSTRAINT strategies_pk_id PRIMARY KEY(id)
);

CREATE UNIQUE INDEX strategies_uq_name ON strategies (name);


DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
  id BIGSERIAL NOT NULL,
  instrument_id BIGINT NOT NULL,
  side VARCHAR(4) NOT NULL,
  amount BIGINT NOT NULL,
  price DECIMAL NOT NULL,
  status VARCHAR(16) NOT NULL,

  x_created_on TIMESTAMP NOT NULL,
  x_updated_on TIMESTAMP,

  CONSTRAINT orders_pk_id PRIMARY KEY(id),
  CONSTRAINT orders_fk_instrument_id FOREIGN KEY (instrument_id) REFERENCES instruments (id)
);


DROP TABLE IF EXISTS transactions;
CREATE TABLE transactions (
  id BIGSERIAL NOT NULL,
  broker_id BIGINT NOT NULL,
  open_on TIMESTAMP NOT NULL,
  close_on TIMESTAMP,
  open_order_id BIGINT NOT NULL,
  close_order_id BIGINT,
  pips_profit DECIMAL,
  strategy_id BIGINT NOT NULL,
  status VARCHAR(16) NOT NULL,

  x_created_on TIMESTAMP NOT NULL,
  x_updated_on TIMESTAMP,

  CONSTRAINT transactions_pk_id PRIMARY KEY(id),
  CONSTRAINT transactions_fk_broker_id FOREIGN KEY (broker_id) REFERENCES brokers (id),
  CONSTRAINT transactions_fk_open_order_id FOREIGN KEY (open_order_id) REFERENCES orders (id),
  CONSTRAINT transactions_fk_close_order_id FOREIGN KEY (close_order_id) REFERENCES orders (id),
  CONSTRAINT transactions_fk_strategy_id FOREIGN KEY (strategy_id) REFERENCES strategies (id)
);

CREATE INDEX transactions_idx_open_order_id ON transactions (open_order_id)
  WHERE open_order_id IS NOT NULL AND close_order_id IS NULL;
CREATE INDEX transactions_idx_open_order_id_close_order_id ON transactions (open_order_id, close_order_id);
  WHERE open_order_id IS NOT NULL AND close_order_id IS NOT NULL;
CREATE INDEX transactions_idx_strategy_id ON transactions (strategy_id);


DROP TABLE IF EXISTS brokers;
CREATE TABLE brokers (
  id BIGSERIAL NOT NULL,
  name VARCHAR(255) NOT NULL,

  x_created_on TIMESTAMP NOT NULL,
  x_updated_on TIMESTAMP,
  x_deleted_on TIMESTAMP,

  CONSTRAINT brokers_pk_id PRIMARY KEY(id)
);

CREATE UNIQUE INDEX brokers_uq_name ON brokers (name);
