-- comentar indices ya existentes
CREATE INDEX idx_historico_fecha on historico USING btree (fecha);	
CREATE INDEX idx_historico_idaccion on historico USING hash (idaccion);
CREATE INDEX idx_saldo_portafolio on portafolio_saldohistorico USING hash (portafolio_idportafolio);
CREATE INDEX saldo_fecha_idx ON portafolio_saldohistorico USING btree (saldohistorico_key);
CREATE INDEX idx_clienteaccion_clipaquete ON clienteaccion (acc_idportafolio,acc_idpaq);