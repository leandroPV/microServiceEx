-- ADD TABLE CREATE SQL
CREATE TABLE "address"
  (
     "tx_id_address" UUID NOT NULL,
     "tx_bairro"     VARCHAR(255),
     "tx_cidade"     VARCHAR(255),
     "tx_estado"     VARCHAR(255),
     "tx_logradouro" VARCHAR(255),
     "tx_id_user"    UUID,
     PRIMARY KEY ("tx_id_address")
  );

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
     "tx_phone"   VARCHAR(255) NOT NULL,
     "tx_cep"     VARCHAR(255),
     "tx_sexo"    VARCHAR(20) NOT NULL,
     PRIMARY KEY ("tx_id_user")
  );

ALTER TABLE IF EXISTS "address" ADD CONSTRAINT "FK41q9imeyirbthrcl25cq7wi5t" FOREIGN KEY ("tx_id_user") REFERENCES "user";
ALTER TABLE IF EXISTS "contact" ADD CONSTRAINT "FKgq2202ng9pmbde3ih3qliq0e" FOREIGN KEY ("tx_id_user") REFERENCES "user";
