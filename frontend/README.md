# Plataforma de Aulas Particulares (TCC) - Frontend

![Angular](https://img.shields.io/badge/Angular-16+-DD0031?logo=angular&logoColor=white)
![Backend](https://img.shields.io/badge/Backend-Spring_Boot-6DB33F?logo=spring&logoColor=white)
![Status](https://img.shields.io/badge/Status-Em_Desenvolvimento-blue)

Este é o repositório do **frontend** do projeto de TCC (INSTRUINDO), uma plataforma para conectar alunos e professores para aulas particulares. O projeto utiliza Angular 16+ e consome a API RESTful construída em Java com Spring Boot.

## 🚀 Funcionalidades Principais

Todo o fluxo do usuário, desde o cadastro até o agendamento de aulas, foi implementado, com foco em uma arquitetura limpa e responsiva.

### 1. Sistema de Autenticação e Segurança
* **Autenticação Completa:** Fluxos de Login, Cadastro e Recuperação de Senha.
* **Validação:** Formulários Reativos (Reactive Forms) com validação robusta.
* **Controle de Acesso:** Uso de `AuthGuard` para proteger rotas privadas (ex: `/painel`).
* **Autorização:** Lógica baseada em Roles (Papéis) para `ROLE_ALUNO` e `ROLE_PROFESSOR`.

### 2. Páginas Públicas
* **Home:** Página de aterrissagem (landing page) responsiva.
* **Busca de Professores (`/search`):** Página de busca com filtros e paginação.
* **Perfil de Professor (`/perfil/:id`):** Página pública detalhada do professor, mostrando suas informações, avaliações e calendário de disponibilidade.

### 3. Painel de Controle (Dashboard)
Refatorado de um layout de "abas" para um painel moderno com navegação lateral (`mat-sidenav`) e roteamento-filho (`<router-outlet>`).

#### Painel do Professor (`ROLE_PROFESSOR`)
* **Início:** Dashboard principal com estatísticas e ações rápidas.
* **Pedidos de Aula:** Lista de solicitações pendentes (Aceitar/Recusar).
* **Gerenciar Horários:** Interface para definir disponibilidade recorrente e bloqueios.
* **Calendário de Aulas:** Visualização das aulas já confirmadas.
* **Informações (Perfil):** Formulário completo para edição de perfil (upload de foto, preço, etc.).
* **Avaliações Recebidas:** Feed de avaliações recebidas.
* **Relatórios:** Ferramenta para gerar relatórios de faturamento.
* **Desativação de Conta:** Opção de soft-delete do perfil.

#### Painel do Aluno (`ROLE_ALUNO`)
* **Início:** Dashboard de boas-vindas com ações rápidas.
* **Minhas Solicitações:** Acompanhamento do status (`PENDENTE`, `ACEITA`, `RECUSADA`).
* **Histórico de Aulas:** Lista de aulas concluídas com opção de avaliar o professor.
* **Informações (Perfil):** Edição de dados básicos e upload/remoção de foto.

## 🛠️ Stack de Tecnologias

* **Frontend:** Angular 16+
* **UI Framework:** Angular Material
* **Linguagem:** TypeScript
* **CSS:** SCSS (Sass)
* **Backend (Consumido):** API RESTful em Java com Spring Boot (rodando em `http://localhost:8080`)

## ⚙️ Pré-requisitos

1.  **Node.js:** Versão 18 ou superior.
2.  **Angular CLI:** `npm install -g @angular/cli`
3.  **Backend:** O servidor backend (Spring Boot) deve estar em execução na porta `http://localhost:8080`.

## 🏁 Como Executar Localmente

1.  **Clone o Repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/repositorio-frontend.git](https://github.com/seu-usuario/repositorio-frontend.git)
    ```

2.  **Entre na Pasta do Projeto:**
    ```bash
    cd repositorio-frontend
    ```

3.  **Instale as Dependências:**
    ```bash
    npm install
    ```

4.  **Execute o Servidor de Desenvolvimento:**
    ```bash
    ng serve
    ```

5.  **Abra o Projeto:**
    * Acesse `http://localhost:4200/` no seu navegador.
