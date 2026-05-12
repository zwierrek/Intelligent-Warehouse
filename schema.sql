-- ══════════════════════════════════════════════════
--   Intelligent Warehouse — Database Schema
-- ══════════════════════════════════════════════════

CREATE TABLE IF NOT EXISTS "AGD" (
    "Id"       INTEGER  PRIMARY KEY,
    "Name"     TEXT     NOT NULL,
    "Quantity" INTEGER  NOT NULL,
    "Price"    REAL     NOT NULL
);
