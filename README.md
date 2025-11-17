# INSTRUINDO - Plataforma de Aulas Particulares (TCC)

![Status](https://img.shields.io/badge/Status-Em_Desenvolvimento-blue)
![Frontend](https://img.shields.io/badge/Frontend-Angular-DD0031?logo=angular&logoColor=white)
![Backend](https://img.shields.io/badge/Backend-Spring_Boot-6DB33F?logo=spring&logoColor=white)

Este é o repositório principal do projeto de TCC "INSTRUINDO", um sistema completo para agendamento de aulas particulares.

## 🚀 Sobre o Projeto

O INSTRUINDO é uma plataforma web que conecta alunos buscando aulas e professores dispostos a ensinar. O sistema gerencia desde a busca e visualização de perfis de instrutores até o agendamento e a avaliação das aulas.

Este repositório é um **monorepo**, o que significa que ele contém os códigos-fonte das duas principais aplicações que compõem o sistema: o **Frontend** e o **Backend**.

## 🏛️ Estrutura do Repositório

O projeto está dividido em duas pastas principais. Cada pasta contém seu próprio `README.md` com instruções específicas de instalação e execução.

### 1. Frontend (Cliente)

* **Descrição:** Aplicação *client-side* construída em Angular. É responsável por toda a interface do usuário e interação (telas de login, cadastro, painéis de aluno e professor, etc.).
* **Pasta:** `./frontend/`
* **Para ver detalhes, clique aqui:** ➡️ **[README do Frontend](./frontend/README.md)**

### 2. Backend (Servidor)

* **Descrição:** API RESTful construída em Java com Spring Boot. É responsável pela lógica de negócio, segurança (JWT) e comunicação com o banco de dados (MariaDB).
* **Pasta:** `./backend/`
* **Para ver detalhes, clique aqui:** ➡️ **[README do Backend](./backend/README.md)**

## 🛠️ Stack Principal

| Área | Tecnologia |
| :--- | :--- |
| **Frontend** | Angular 16+, Angular Material, TypeScript, SCSS |
| **Backend** | Java 25, Spring Boot 3.5+, Spring Security (JWT) |
| **Banco de Dados** | MariaDB |
| **Documentação** | Springdoc OpenAPI (Swagger) |

---