-- ADD TABLE CREATE SQL
CREATE TABLE "contact"
  (
     "tx_id_contact" UUID NOT NULL,
     "dt_created"    TIMESTAMP DEFAULT NOW(),
     "dt_updated"    TIMESTAMP,
     "tx_value"      VARCHAR(255) NOT NULL,
     "tx_id_user"    UUID NOT NULL,
     PRIMARY KEY ("tx_id_contact")
  );

CREATE TABLE "user"
  (
     "tx_id_user" UUID NOT NULL,
     "dt_created" TIMESTAMP DEFAULT NOW(),
     "dt_updated" TIMESTAMP,
     "tx_cpf"     VARCHAR(255) NOT NULL,
     "tx_name"    VARCHAR(255) NOT NULL,
     PRIMARY KEY ("tx_id_user")
  );

ALTER TABLE IF EXISTS "contact" ADD CONSTRAINT "FKgq2202ng9pmbde3ih3qliq0e" FOREIGN KEY ("tx_id_user") REFERENCES "user";
