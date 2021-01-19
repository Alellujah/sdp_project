<html>
<header>
    <title>SDP Project</title>
</header>
<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.4.1/semantic.min.css">
<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.4.1/components/modal.min.css">
<script src="https://code.jquery.com/jquery-3.1.1.min.js" integrity="sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8=" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.4.1/semantic.min.js"></script>
<script src=" https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.4.1/components/modal.min.js"></script>
<style>
    h1 {
        padding: 1rem;
        margin: 0;
    }

    li {
        list-style: none;
        padding: 1rem;
        border-bottom: 1px solid #eee;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    form {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .add {
        padding: 1rem;
    }
</style>

<body>
    <section>
        <h1>Items</h1>
        <div id="items"></div>
        <div class="add">
            <form id="itemAdd" class="ui form">
                <div class="fields">
                    <div class="field">
                        <input id="itemName" placeholder="Nome" />
                    </div>
                    <div class="field">
                        <input id="itemDesc" placeholder="Descricao" />
                    </div>
                </div>
                <button class="ui button primary" type="submit">Add</button>
            </form>
        </div>
    </section>
    <section>
        <h1>Armazem</h1>
        <div id="armazem"></div>
        <form id="armazemAdd" class="ui form add">
            <div class="fields ">
                <div class="field">
                    <select name="armazem" id="armazemSelect" placeholder="Selecionar um produto"></select>
                </div>
                <div class="field">
                    <input id="itemStock" placeholder="Stock para depositar" />
                </div>
            </div>
            <button class="ui button primary" type="submit">Add</button>
        </form>
        <button style="width:100%" class="ui button danger" id="deleteAll"> Delete all storage</button>
    </section>
    <section>
        <h1>Entregas</h1>
        <div id="entregas"></div>
        <form id="entregasAdd" class="ui form add">
            <div class="fields">
                <div class="field">
                    <select name="entregas" id="entregasSelect" placeholder="Selecionar um produto">
                    </select>
                </div>
                <div class="field">
                    <input id="localEntrega" placeholder="Local para entrega" />
                </div>
                <div class="field">
                    <input id="qtdEntrega" placeholder="Quantidade" />
                </div>
            </div>
            <button class="ui button primary" type="submit">Add</button>
        </form>
    </section>
    <div class="ui modal">
        <i class="close icon"></i>
        <div class="header">
            Edit Descrição
        </div>
        <div class="content">
            <input disabled id="editItemId" />
            <input id="editName" />
            <input id="editDesc" placeholder="New description"/>
        </div>
        <div class="actions">            
            <div class="ui button primary" id="saveNewDesc">Save</div>
        </div>
    </div>
    <div class="ui modal" id="deliveryEdit">
        <i class="close icon"></i>
        <div class="header">
            Edit Entrega
        </div>
        <div class="content">
            <input disabled id="editDeliveryId" />
            <input disabled id="editDeliveryItemId" />
            <input disabled id="editQtd" />      
            <input id="editLocal" />      
        </div>
        <div class="actions">            
            <div class="ui button primary" id="saveNewDelivery">Save</div>
        </div>
    </div>
</body>
<script>
    // HEADERS
    var myHeaders = new Headers({
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    });
    var getHeaders = {
        method: "GET",
        headers: myHeaders,
        mode: "cors",
    };
    var postHeaders = {
        method: "POST",
        headers: myHeaders,
        mode: "cors",
    };
    var putHeaders = {
        method: "PUT",
        headers: myHeaders,
        mode: "cors",
    };
    var deleteHeaders = {
        method: "DELETE",
        headers: myHeaders,
        mode: "cors",
    };
    // GET
    var getItems = new Request("http://localhost:8080/api/v1/item", getHeaders);
    var getItemsInStorage = new Request("http://localhost:8080/api/v1/armazem", getHeaders);
    var getDeliveries = new Request("http://localhost:8080/api/v1/delivery", getHeaders);
    // POST
    var postItem = new Request("http://localhost:8080/api/v1/createItem", postHeaders);
    var postDelivery = new Request("http://localhost:8080/api/v1/createDelivery", postHeaders);
    //var postDeposit = new Request("http://localhost:8080/api/v1/depositItem/id", postHeaders);
    // PUT
    var putItem = new Request("http://localhost:8080/api/v1/item/id", putHeaders);
    var putUpdateItemInStorage = "http://localhost:8080/api/v1/updateStoragedItem/id"
    // DELETE
    //var deleteItem = new Request("http://localhost:8080/api/v1/items/id", deleteHeaders);
    var deleteStoragedItem = new Request("http://localhost:8080/api/v1/deleteStoragedItem/id", deleteHeaders);
    var deleteAllStorage = new Request("http://localhost:8080/api/v1/deleteAllStoragedItem", deleteHeaders);
    var deleteEntrega = new Request("http://localhost:8080/api/v1/deleteDelivery/id", deleteHeaders);
    // OnLoad
    document.addEventListener("DOMContentLoaded", function() {
        const itemsDiv = document.querySelector('#items')
        fetch(getItems).then(function(response) {
            if (response.ok) {
                response.json().then(data => {
                    data.forEach(item => {
                        console.log(item)
                        //btn
                        const btn = document.createElement("button");
                        const editBtn = document.createElement("button");
                        const span = document.createElement("span");
                        btn.innerText = 'Delete';
                        btn.classList.add('ui');
                        btn.classList.add('danger');
                        btn.classList.add('button');
                        editBtn.innerText = 'Edit';
                        editBtn.classList.add('ui');
                        editBtn.classList.add('primary');
                        editBtn.classList.add('button');
                        btn.onclick = function() {
                            fetch("http://localhost:8080/api/v1/item/" + item.id, deleteHeaders).then(function(response) {
                                console.log(response);
                                // location.reload();
                            })
                        };
                        editBtn.onclick = function() {
                            $('.ui.modal').modal('show');
                            const desc = item.descricao;
                            document.querySelector("#editItemId").value = item.id;
                            document.querySelector("#editName").value = item.nome;
                            document.querySelector("#editDesc").value = desc;
                        }
                        //li
                        const element = document.createElement("LI");
                        const textNode = document.createTextNode(`Nome: ${item.nome} , Descricao: ${item.descricao}`);
                        element.appendChild(textNode);
                        itemsDiv.append(element);
                        span.append(btn);
                        span.append(editBtn);
                        element.append(span);
                        // select
                        const armazemSelect = document.querySelector('#armazemSelect');
                        const entregasSelect = document.querySelector('#entregasSelect');
                        const optionEl = document.createElement("option");
                        const optionNode = document.createTextNode(item.nome);
                        optionEl.appendChild(optionNode);
                        optionEl.value = item.id;
                        armazemSelect.append(optionEl);
                        var cln = optionEl.cloneNode(true);
                        entregasSelect.append(cln);
                    })
                })
            }
        });
        const armazemDiv = document.querySelector('#armazem')
        fetch(getItemsInStorage).then(function(response) {
            if (response.ok) {
                response.json().then(data => {
                    data.forEach(lista => {
                        console.log(lista)
                      
                        // LI
                        lista.lista.forEach(data => {
                              //btn
                        const btn = document.createElement("button");
                        btn.innerText = 'Delete';
                        btn.classList.add('ui');
                        btn.classList.add('danger');
                        btn.classList.add('button');
                        btn.onclick = function() {
                            fetch("http://localhost:8080/api/v1/deleteStoragedItem/" + data.id, deleteHeaders).then(function(response) {
                                console.log(response);
                                if (response.ok) {
                                    location.reload();
                                } else {
                                    console.log('error!')
                                }
                            })
                        };
                            const element = document.createElement("LI");
                            console.log('data', data)
                            const textNode = document.createTextNode(`Nome: ${data.item.nome} , Descricao: ${data.item.descricao}, Stock: ${data.quantidade}`);
                            element.appendChild(textNode);
                            armazemDiv.append(element);                        
                            element.append(btn);
                        })
                        
                    })
                })
            }
        });
        const entregasDiv = document.querySelector('#entregas')
        fetch(getDeliveries).then(function(response) {
            if (response.ok) {
                response.json().then(data => {
                    data.forEach(data => {
                        console.log(data)
                        //btn
                        const btn = document.createElement("button");
                        const editBtn = document.createElement("button");
                        const span = document.createElement("span");

                        btn.innerText = 'Delete';
                        btn.classList.add('ui');
                        btn.classList.add('danger');
                        btn.classList.add('button');
                        editBtn.innerText = 'Edit';
                        editBtn.classList.add('ui');
                        editBtn.classList.add('primary');
                        editBtn.classList.add('button');
                        btn.onclick = function() {
                            fetch("http://localhost:8080/api/v1/deleteDelivery/" + data.id, deleteHeaders).then(function(response) {
                                console.log(response);
                                if (response.ok) {
                                    location.reload();
                                } else {
                                    console.log('error!')
                                }
                            })
                        };
                        editBtn.onclick = function() {
                            $('#deliveryEdit').modal('show');
                            document.querySelector("#editDeliveryId").value = item.id;                            
                            document.querySelector("#editLocal").value = item.localEntrega;
                            document.querySelector("#editDeliveryItemId").value = item.itemId;                            
                        }
                        
                        // LI
                        const element = document.createElement("LI");
                        const h3 = document.createElement("h3");
                        const local = document.createTextNode(`Local: ${data.localEntrega}`);
                        h3.appendChild(local);
                        element.append(h3);
                        data.list.forEach(item => {
                            const textNode = document.createTextNode(`Nome: ${item.item.nome}, Descriçao: ${item.item.descricao} , Quantidade: ${item.quantidade}`);                                                    
                            element.appendChild(textNode);
                        entregasDiv.append(element);
                        element.append(document.createElement("br"))
                        })
                        
                        element.style.display = "flow-root";
                      
                        // span.append(editBtn);
                        span.append(btn);
                        element.append(span);
                    })
                })
            }
        });
    });
    // POST 
    document.getElementById("itemAdd").addEventListener("submit", (e) => {
        e.preventDefault();
        const newName = document.querySelector("#itemName").value
        const newDesc = document.querySelector("#itemDesc").value
        console.log('newname', newName)
        fetch(postItem, {
            body: JSON.stringify({
                "nome": newName,
                "descricao": newDesc,
                "quantidade": 0
            })
        }).then(function(response) {
            console.log(response)
            location.reload();
        })
    });
    document.getElementById("armazemAdd").addEventListener("submit", (e) => {
        e.preventDefault();
        const itemId = document.querySelector("#armazemSelect").value
        const newItemStock = document.querySelector("#itemStock").value
        console.log('item id', itemId);
        console.log('new stock', newItemStock);
        fetch("http://localhost:8080/api/v1/depositItem/" + itemId, {
            ...postHeaders,
            body: newItemStock           
        }).then(function(response) {
            console.log(response)
            location.reload();
        })
    });
    document.getElementById("entregasAdd").addEventListener("submit", (e) => {
        e.preventDefault();
        const itemId = document.querySelector("#entregasSelect").value
        const quantidadeItem = document.querySelector("#qtdEntrega").value
        const localEntrega = document.querySelector("#localEntrega").value
        fetch(postDelivery, {
            body: JSON.stringify([{
                "items": {itemId: 5}, //id / qtd
                "local": localEntrega
            }])
        }).then(function(response) {
            console.log(response)
            location.reload();
        })
    });
    // PUT    
    document.getElementById("saveNewDesc").addEventListener("click", (e) => {
        e.preventDefault();
        const itemId = document.querySelector("#editItemId").value;
        const newDesc = document.querySelector("#editDesc").value;
        const newName = document.querySelector("#editName").value;
        fetch("http://localhost:8080/api/v1/items/" + itemId, {
            ...putHeaders,
            body: JSON.stringify({
                "nome": newName,
                "descricao": newDesc,
            })
        }).then(function(response) {
            console.log(response);
            if (response.ok) {
                location.reload();
            } else {
                console.log('error!')
            }
        })
    });   
    document.getElementById("saveNewDelivery").addEventListener("click", (e) => {
        e.preventDefault();
        const deliveryId = document.querySelector("#editDeliveryId").value;
        const newLocal = document.querySelector("#editLocal").value;
        const newDeliveryItemId = document.querySelector("#editDeliveryItemId").value;
        const newQtd = document.querySelector("#editQtd").value;
        fetch("http://localhost:8080/api/v1/updateDelivery/" + deliveryId, {
            ...putHeaders,
            body: JSON.stringify({
                "itemId": newDeliveryItemId,
                "quantidadeItem": newQtd,
                "localEntrega": newLocal
            })
        }).then(function(response) {
            console.log(response);
            if (response.ok) {
                location.reload();
            } else {
                console.log('error!')
            }
        })
    });   
    // DELETE
    document.getElementById("deleteAll").addEventListener("click", (e) => {
        e.preventDefault();
        fetch("http://localhost:8080/api/v1/deleteAllStoragedItem/", deleteHeaders).then(function(response) {
            console.log(response);
            if (response.ok) {
                location.reload();
            } else {
                console.log('error!')
            }
        })
    });   
</script>

</html>