# [Nimble-gestão-combraça]

Breve descrição do projeto e seu propósito (Ex: API RESTful para gerenciamento de cobranças, Sistema de Cadastro de Usuários, etc.).

## 🚀 Tecnologias e Configurações

Este projeto foi desenvolvido utilizando as seguintes tecnologias principais:

* **Linguagem:** Java 21+
* **Framework:** Spring Boot 3
* **Segurança:** Spring Security (OAuth2 Resource Server) com JWT (JSON Web Token)
* **Banco de Dados:** PostgreSQL (Configuração via Docker Compose)

## 📖 Documentação da API (Swagger UI)

A documentação interativa completa da API, incluindo todos os endpoints, modelos de dados e o mecanismo de autenticação, está disponível através do Swagger UI.

Para acessar a documentação:

O Ideal para teste por conta de problema de autorização é testar ele pelo postman/insomia, pelo que vi a versão estava bugado a autorização então eu não conseguia testar os endpoint pelo UI.

1.  Inicie a aplicação.
2.  Acesse a URL no seu navegador: `http://localhost:[PORTA]/swagger-ui/index.html`

### Autenticação (JWT Bearer Token)

Todos os endpoints protegidos exigem um token JWT válido, obtido pelo endpoint de `/login`.

Obs: Eu não consegui configurar a questão da autorização no swagger então o botão de autorização da ui-não está funcionando.

## ⚙️ Como Rodar o Projeto

### 1. Inicializar o Banco de Dados (PostgreSQL via Docker)

O banco de dados é inicializado usando o Docker Compose para garantir um ambiente consistente.

Para subir o contêiner do PostgreSQL e criar o banco de dados conforme as configurações no `docker-compose.yml`, execute o seguinte comando no diretório raiz do projeto:

```bash
docker compose up -d postgres

### 2. Iniciar a aplicação

Após isso rodar a aplicação.

## ⚠️ Sobre Testes Unitários e de Integração

Devido à alta demanda e aos prazos apertados nos projetos da empresa onde eu trabalho, **não foi possível implementar a cobertura de testes unitários e de integração** para esta entrega.
