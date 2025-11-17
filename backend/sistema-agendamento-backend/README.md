# Plataforma de Aulas Particulares (TCC) - Backend

![Java](https://img.shields.io/badge/Java-25+-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.7-6DB33F?logo=spring&logoColor=white)
![Status](https://img.shields.io/badge/Status-Em_Desenvolvimento-blue)

Este é o repositório do **backend** do projeto de TCC (INSTRUINDO), uma API RESTful para o Sistema de Agendamento de Aulas. A API é construída em Java 25 com Spring Boot e utiliza Spring Security com JWT.

## 🚀 Funcionalidades Principais

A API gerencia toda a lógica de negócio, segurança e persistência de dados da plataforma.

### 1. Sistema de Autenticação e Segurança (Spring Security)
* **Autenticação JWT:** Fluxo completo de Cadastro, Login (geração de token JWT) e Recuperação de Senha.
* **Autorização:** Controle de acesso baseado em Papéis (Roles) para `ROLE_ALUNO`, `ROLE_PROFESSOR` e `ROLE_ADMIN`.
* **Segurança:** Configuração de CORS, CSRF e proteção de endpoints.
* **Soft Delete:** Desativação de conta de usuário (lógica de "usuário ativo").

### 2. Lógica de Negócio (Core)
* **Gestão de Agenda:** Professores podem definir disponibilidade semanal recorrente (ex: Segundas, 14h-16h) e adicionar bloqueios de horário específicos (ex: "Férias", "Médico").
* **Fluxo de Agendamento:** Endpoints para alunos solicitarem aulas (com cálculo de horários livres), e para professores aceitarem/recusarem (com verificação automática de conflito).
* **Gestão de Perfis:** Endpoints para alunos e professores atualizarem seus dados e fotos.

### 3. Funcionalidades de Suporte
* **Comunicação:** Sistema de Notificações (in-app e por e-mail via Spring Boot Mail).
* **Feedback:** Fluxo de avaliação (Aluno avalia Aula/Professor).
* **Relatórios:** Endpoints de dashboard e relatórios por período.
* **Documentação:** API 100% documentada com Swagger (Springdoc OpenAPI).

## 🛠️ Stack de Tecnologias

* **Linguagem:** Java 25
* **Framework:** Spring Boot 3.5.7
* **Segurança:** Spring Security 6 (com autenticação JWT)
* **Banco de Dados:** Spring Data JPA (Hibernate) + MariaDB
* **Email:** Spring Boot Mail (Integração com Gmail)
* **Documentação:** Springdoc OpenAPI (Swagger UI)
* **Build:** Maven
* **Utilitários:** Lombok

## ⚙️ Pré-requisitos

1.  **Java JDK 25** (ou superior).
2.  **Maven** (para gestão de dependências).
3.  **MariaDB (Servidor):** Um servidor MariaDB deve estar em execução (ex: `localhost:3306`).
4.  **Conta Google (Gmail):** Necessário para o serviço de e-mail. Requer **Verificação de 2 Passos ATIVADA** e uma **"Senha de App"** de 16 letras gerada.

## 🏁 Como Executar Localmente

1.  **Prepare o Banco de Dados:**
    * O Spring Boot (via `ddl-auto=update`) criará as *tabelas* automaticamente, mas precisa que a *base de dados* (o "schema") exista.
    * Execute o seguinte comando SQL no seu MariaDB:
    ```sql
    CREATE DATABASE IF NOT EXISTS tcc_agendamento_db
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;
    ```

2.  **Clone o Repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/repositorio-backend.git](https://github.com/seu-usuario/repositorio-backend.git)
    ```

3.  **Entre na Pasta do Projeto:**
    ```bash
    cd repositorio-backend
    ```

4.  **Configure as Variáveis de Ambiente:**
    * No arquivo `src/main/resources/application.properties`, atualize as seguintes seções:
    * **Banco de Dados:** `spring.datasource.username` e `spring.datasource.password`.
    * **Email (Gmail):** `spring.mail.username` (seu email) e `spring.mail.password` (sua Senha de App de 16 letras).

5.  **Instale as Dependências (Via Maven):**
    ```bash
    mvn clean install
    ```

6.  **Execute o Servidor de Desenvolvimento:**
    * **Opção 1 (Linha de Comando):**
        ```bash
        mvn spring-boot:run
        ```
    * **Opção 2 (VSCode):**
        * Use o perfil "Run and Debug" (F5) do VSCode. (Requer a extensão "Extension Pack for Java").

7.  **Abra o Projeto:**
    * A API estará em execução em `http://localhost:8080/`.
    * Acesse a documentação do Swagger em `http://localhost:8080/swagger-ui.html`.