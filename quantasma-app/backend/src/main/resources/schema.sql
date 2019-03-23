DROP TABLE IF EXISTS instruments;
DROP TABLE IF EXISTS strategies;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS transactions;

CREATE TABLE instruments (
  id BIGSERIAL,
  name VARCHAR(255),
  precision SMALLINT,

  x_created_on TIMESTAMP,
  x_updated_on TIMESTAMP,
  x_deleted_on TIMESTAMP,

  CONSTRAINT pk_t_instruments PRIMARY KEY(id)
);

CREATE TABLE strategies (
  id BIGSERIAL,
  name VARCHAR(255),
  class VARCHAR(255),
  checksum VARCHAR(255),
  active BOOLEAN,

  x_created_on TIMESTAMP,
  x_updated_on TIMESTAMP,
  x_deleted_on TIMESTAMP,

  CONSTRAINT pk_t_strategies PRIMARY KEY(id)
);

CREATE TABLE orders (
  id BIGSERIAL,
  instrument_id BIGINT,
  side VARCHAR(4),
  amount BIGINT,
  price DECIMAL,

  x_created_on TIMESTAMP,

  CONSTRAINT pk_t_orders PRIMARY KEY(id)
);

CREATE TABLE transactions (
  id BIGINT,
  open_ts TIMESTAMP,
  close_ts TIMESTAMP,
  open_order_id BIGINT,
  close_order_id BIGINT,
  pips_profit DECIMAL,
  strategy_id BIGINT,

  x_created_on TIMESTAMP,
  x_updated_on TIMESTAMP,

  CONSTRAINT pk_t_transactions PRIMARY KEY(id)
);
