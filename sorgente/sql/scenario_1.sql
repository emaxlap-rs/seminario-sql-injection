CREATE OR REPLACE SCHEMA login AUTHORIZATION postgres;

CREATE OR REPLACE TABLE login.users (
	username VARCHAR(20) PRIMARY KEY,
	password VARCHAR(20) NOT NULL
);

INSERT INTO login.users (username, password) VALUES
('admin', 'complex password'),
('user', 'password123'),
('example', 'anotherpassword123'),
('Cyber_Gnomo_404', 'Neon_Pulse_99'),
('Bit_Buster_Zero', 'K3y_Phon1x!'),
('Sintassi_Errata', 'Null_Pointer_Ex'),
('Galactic_Muffin', 'Andromeda_2026'),
('Lupo_Di_Pixel', 'Luna_D_Argento'),
('Quercia_Quantistica', 'Radici_001'),
('Nebulosa_Azzurra', 'Stardust_Vibe'),
('Spirito_D_Inverno', 'Ghiaccio_Bollente'),
('Pizza_Senza_Bordi', 'Extra_Mozzarella'),
('Caffeina_Cronica', 'Espresso_Doppio_9'),
('Pinguino_In_Pigiama', 'Antartide_Warm'),
('Tostapane_Ribelle', 'Burn_The_Bread!'),
('Eco_Del_Silenzio', 'Shhh_Secret'),
('Viaggiatore_Onirico', 'Sogno_Lucido_7'),
('Frammento_Di_Tempo', 'Orologio_Rotto');
