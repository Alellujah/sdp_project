CREATE DATABASE sdp;

CREATE TABLE Items (
    ItemId int IDENTITY(1,1) PRIMARY KEY,
    Nome varchar(255) NOT NULL,
    Descricao varchar(255) NOT NULL
);

CREATE TABLE ArmazemCentral (
    ItemId int PRIMARY KEY,
    ItemStock int NOT NULL
);

CREATE TABLE Entregas (
	EntregaId int PRIMARY KEY,
	ItemId int NOT NULL,
	QuantidadeItem int NOT NULL,
	LocalEntrega varchar(255) NOT NULL
);