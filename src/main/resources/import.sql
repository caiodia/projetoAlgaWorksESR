insert into cozinha (id, nome) values (1, 'Tailandesa');
insert into cozinha (id, nome) values (2, 'Indiana');
insert into restaurante (nome,taxa_frete,cozinha_id) values ('Mama Julia', 4.10, 1);
insert into restaurante (nome,taxa_frete,cozinha_id) values ('Pé de Fava', 2.55, 2);

insert into forma_pagamento (descricao) values ('cartao de credito');
insert into forma_pagamento (descricao) values ('cartao de credito - parcelado');
insert into forma_pagamento (descricao) values ('cartao de debito');

insert into Estado (nome) values ('Sao Paulo');
insert into cidade (nome) values ('Sao Paulo');

insert into Estado (nome) values ('Sao Paulo');
insert into cidade (nome) values ('Guarulhos');

insert into Estado (nome) values ('Rio de Janeiro');
insert into cidade (nome) values ('Sao Goncalo');

insert into permissao (nome, descricao) values ('Consulta', 'Todos os usuarios possuem acesso para visualização do produto')
insert into permissao (nome, descricao) values ('Compra', 'usuario com perfil de compra')