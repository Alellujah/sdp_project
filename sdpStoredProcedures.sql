CREATE PROCEDURE CreateItem
	@Nome VARCHAR(255),
    @Descricao VARCHAR(255) 
AS 
	BEGIN 
		SET NOCOUNT ON
		INSERT INTO Items(Nome, Descricao) 
		VALUES (@Nome, @Descricao)
	END 
GO

CREATE PROCEDURE ReadItem
AS 
	BEGIN 
		SELECT Items.Nome, Items.Descricao, ArmazemCentral.ItemStock
		FROM ArmazemCentral INNER JOIN Items ON ArmazemCentral.ItemId = Items.ItemId
	END 
GO

CREATE PROCEDURE UpdateItem
	@ItemId INT,
	@Descricao VARCHAR(255)
AS
	BEGIN
		UPDATE Items
		SET Descricao = @Descricao
		WHERE ItemId = @ItemId
	END
GO

CREATE PROCEDURE DeleteItem
	@ItemId INT
AS
	BEGIN
	DECLARE @Result BIT
	SET @Result = 0

	IF EXISTS(SELECT TOP 1 ArmazemCentral.ItemId FROM ArmazemCentral WHERE ArmazemCentral.ItemId = @ItemId)
		BEGIN
			RETURN (@Result)
		END
	IF EXISTS(SELECT TOP 1 Entregas.ItemId FROM Entregas WHERE Entregas.ItemId = @ItemId)
		BEGIN
			RETURN (@Result)
		END

	DELETE FROM Items WHERE ItemId = @ItemId;
	SET @Result = 1
	RETURN (@Result)
	END
GO

CREATE PROCEDURE CreateUpdateArmazemCentral
	@ItemId INT,
	@AddItem INT
AS
	DECLARE @Result BIT
	DECLARE @Stock INT
	BEGIN
		IF EXISTS(SELECT TOP 1 Items.ItemId FROM Items WHERE Items.ItemId = @ItemId)
			BEGIN
			IF NOT EXISTS(SELECT TOP 1 ArmazemCentral.ItemId FROM ArmazemCentral WHERE ArmazemCentral.ItemId = @ItemId)
				BEGIN
				INSERT INTO ArmazemCentral(ItemId, ItemStock)
				VALUES (@ItemId, @AddItem)
				SET @Result = 1
				RETURN (@Result)
				END
				UPDATE ArmazemCentral
				SET @Stock = (SELECT ArmazemCentral.ItemStock FROM ArmazemCentral WHERE ArmazemCentral.ItemId = @ItemId) + @AddItem
				WHERE ArmazemCentral.ItemId = @ItemId;
				SET @Result = 1
				RETURN (@Result)
			END
			SET @Result = 0
			RETURN (@Result)
	END
GO

CREATE PROCEDURE ReadArmazemCentral
AS
	BEGIN
		SELECT Items.Nome, Items.Descricao, ArmazemCentral.ItemStock
		FROM ArmazemCentral INNER JOIN Items ON ArmazemCentral.ItemId = Items.ItemId
		WHERE ArmazemCentral.ItemStock > 0 
	END
GO

CREATE PROCEDURE CreateEntrega
	@ItemId INT,
	@QuantidadeItem INT,
	@LocalEntrega VARCHAR(255)
AS
	BEGIN
		INSERT INTO Entregas (EntregaId, ItemId, QuantidadeItem, LocalEntrega)
		VALUES((SELECT IDENT_CURRENT('EntregaId')), @ItemId, @QuantidadeItem, @LocalEntrega)
	END
GO

CREATE PROCEDURE ReadEntrega
AS
	BEGIN
		SELECT * FROM Entregas
	END
GO

CREATE PROCEDURE UpdateEntrega
	@EntregaId INT,
	@LocalEntrega VARCHAR(255)
AS
	BEGIN
		UPDATE Entregas
		SET LocalEntrega = @LocalEntrega
		WHERE EntregaId = @EntregaId
	END
GO