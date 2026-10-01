# 🚀 Workshop MongoDB — Web Service RESTful com Spring Boot e NoSQL

> **API REST completa** construída do zero com **Spring Boot 4.1**, **Java 21** e **Spring Data MongoDB**.  
> Aplicação backend de uma rede social simplificada com **Usuários (Users)**, **Postagens (Posts)** e **Comentários (Comments)** — com buscas inteligentes por texto e intervalo de datas.

---

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21"/>
  <img src="https://img.shields.io/badge/Spring_Boot-4.1.1-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot 4.1.1"/>
  <img src="https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white" alt="MongoDB"/>
  <img src="https://img.shields.io/badge/Build-Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" alt="Maven"/>
  <img src="https://img.shields.io/badge/Status-100%25_Funcional-brightgreen?style=for-the-badge" alt="Status"/>
</p>

---

## 📑 Sumário

- [O que foi feito?](#-o-que-foi-feito)
- [Como foi feito?](#%EF%B8%8F-como-foi-feito)
  - [Arquitetura em Camadas](#arquitetura-em-camadas)
  - [Stack Tecnológica](#stack-tecnológica)
  - [Modelagem NoSQL](#modelagem-nosql-com-mongodb)
- [📡 Endpoints da API](#-endpoints-da-api)
  - [Users](#users)
  - [Posts](#posts)
  - [Buscas Avançadas](#buscas-avan%C3%A7adas-em-posts)
- [🚀 Como rodar o projeto](#-como-rodar-o-projeto)
- [🎓 Lições Aprendidas](#-li%C3%A7%C3%B5es-aprendidas)
- [✅ Status de Validação](#-status-de-valida%C3%A7%C3%A3o)

---

## ✨ O que foi feito?

Desenvolvi um **Web Service RESTful completo** para gerenciar uma plataforma de publicação de conteúdo:

| Funcionalidade | Descrição |
|---|---|
| ✅ **CRUD de Usuários** | Cadastro, consulta, atualização e exclusão de Users |
| ✅ **CRUD de Postagens** | Cadastro, consulta, atualização e exclusão de Posts |
| ✅ **Lista de Posts por Usuário** | Endpoint `/users/{id}/posts` com referência `@DBRef(lazy)` |
| ✅ **Autor embedado no Post** | Uso de `AuthorDTO` aninhado dentro do documento Post (desnormalização NoSQL) |
| ✅ **Comentários em Posts** | Lista de `CommentDTO` embedados diretamente em cada Post |
| ✅ **Busca textual no Título** | Endpoint `/posts/titlesearch` com regex case-insensitive no MongoDB |
| ✅ **Busca FULLTEXT inteligente** | Endpoint `/posts/fullsearch` que pesquisa texto em **título, corpo e comentários**, **filtrando por intervalo de datas** |
| ✅ **Decodificação de URL** | Utilitário `URL.decodeParam()` UTF-8 para buscas com acentos e caracteres especiais |
| ✅ **Conversão de Data GMT** | Utilitário `URL.convertDate()` com fuso GMT e valor default quando a data é inválida |
| ✅ **Tratamento Global de Exceções** | `@ControllerAdvice` + `StandardError` que retorna `404 NOT_FOUND` padronizado com timestamp, path e mensagem |
| ✅ **Padronização em DTOs** | TODOS os endpoints de listagem/consulta retornam DTO, nunca a entidade diretamente (boas práticas de API) |
| ✅ **Seed de dados em runtime** | `CommandLineRunner` que popula o banco automaticamente com usuários e posts ao iniciar |

---

## 🛠️ Como foi feito?

### Arquitetura em Camadas

O projeto segue a arquitetura **MVC / N-Camadas** mantendo cada classe com responsabilidade única:

```
├── domain/          → Entidades (Documentos do MongoDB): User, Post
├── dto/             → Objetos de Transferência: UserDTO, PostDTO, AuthorDTO, CommentDTO
├── repository/      → Acesso ao banco: UserRepository, PostRepository (Spring Data)
├── services/        → Regras de Negócio: UserService, PostService
├── resources/       → Controllers REST: UserResource, PostResource
│   └── util/        → Helpers: URL (decodeParam, convertDate)
│   └── exception/   → ResouceExceptionHandler, StandardError
├── services/exception → ObjectNotFoundException (RuntimeException customizada)
└── config/          → Instantiation (CommandLineRunner de seed)
```

### Stack Tecnológica

| Tecnologia | Versão | Aplicação |
|---|---|---|
| **Java** | 21 LTS | Linguagem base do projeto |
| **Spring Boot** | 4.1.1 | Framework para auto-configuração e inicialização |
| **Spring Data MongoDB** | 5.x | Abstração de persistência + MongoRepository com `@Query` |
| **MongoDB Driver Sync** | 5.8.1 | Driver oficial para conexão com MongoDB Standalone |
| **Spring Web MVC** | 6.x | Endpoints REST (`@RestController`, `@GetMapping`, `@RequestParam`, etc.) |
| **Apache Maven** | 3.9 | Build, dependências e ciclo de vida |
| **JUnit 5 + Mockito** | Integrado | Testes unitários e de contexto |

### Modelagem NoSQL com MongoDB

> 📌 **Diferente de bancos relacionais, o MongoDB é orientado a documentos.**  
> A decisão de **embedar vs referenciar** define toda a performance e usabilidade da aplicação.

| Elemento | Estratégia | Por quê? |
|---|---|---|
| **`Post.author`** | **Embedado** com `AuthorDTO` (id + name) | Em NoSQL evita JOIN — um Post já traz os dados do autor consigo. |
| **`Post.comments`** | **Embedado** como `List<CommentDTO>` | Comentários são fortemente acoplados ao post e consultados juntos. |
| **`User.posts`** | **Referenciado** via `@DBRef(lazy = true)` | Lista de posts por usuário é leve até que você acesse explicitamente via `/users/{id}/posts` |
| **IDs** | `String` (ObjectId) | Padrão nativo do MongoDB — o Spring converte automaticamente. |

#### Exemplo de documentos gerados no MongoDB:

```json
// Coleção "user"
{
  "_id": ObjectId("64f1..."),
  "name": "Maria Brown",
  "email": "maria@gmail.com",
  "posts": [{ "$ref": "post", "$id": ObjectId("64f2...") }]
}
```

```json
// Coleção "post"
{
  "_id": ObjectId("64f2..."),
  "date": ISODate("2018-03-21T00:00:00Z"),
  "title": "Partiu viagem",
  "body": "Vou viajar para São Paulo. Abraços!",
  "author": { "id": "64f1...", "name": "Maria Brown" },
  "comments": [
    { "text": "Boa viagem!", "date": ISODate("2018-03-21T10:00:00Z"), "author": { ... } }
  ]
}
```

---

## 📡 Endpoints da API

Por padrão a API roda em **`http://localhost:8080`** e banco em **`mongodb://localhost:27017/workshop_mongo`**.

---

### Users

| Método | Rota | Body | Retorno | Descrição |
|---|---|---|---|---|
| `GET` | `/users` | - | `List<UserDTO>` | Lista todos os usuários |
| `GET` | `/users/{id}` | - | `UserDTO` | Busca usuário por ID |
| `POST` | `/users` | `UserDTO` (name, email) | `201 Created` + header `Location` | Cria novo usuário |
| `PUT` | `/users/{id}` | `UserDTO` | `204 No Content` | Atualiza dados do usuário |
| `DELETE` | `/users/{id}` | - | `204 No Content` | Remove usuário |
| `GET` | `/users/{id}/posts` | - | `List<PostDTO>` | Todos os posts de um usuário (via `@DBRef lazy`) |

---

### Posts

| Método | Rota | Body | Retorno | Descrição |
|---|---|---|---|---|
| `GET` | `/posts` | - | `List<PostDTO>` | Lista todos os posts |
| `GET` | `/posts/{id}` | - | `PostDTO` | Busca post por ID (inclui author e comments embedados) |
| `POST` | `/posts` | `PostDTO` (date, title, body, author) | `201 Created` + header `Location` | Cria novo post |
| `PUT` | `/posts/{id}` | `PostDTO` | `204 No Content` | Atualiza post |
| `DELETE` | `/posts/{id}` | - | `204 No Content` | Remove post |

---

### Buscas avançadas em Posts

#### 🔎 `titlesearch` — busca no título (case-insensitive)
```http
GET /posts/titlesearch?text=viagem
GET /posts/titlesearch?text=Bom%20dia      ← caracteres codificados são decodificados automaticamente
```

**Engine:** `@Query regex MongoDB` → `{ 'title': { $regex: ?0, $options: 'i' } }`

---

#### 🔎 `fullsearch` — busca em título + corpo + comentários, com intervalo de datas

```http
GET /posts/fullsearch?text=feliz&minDate=2018-03-01&maxDate=2018-03-31
```

| Query Param | Obrigatorio? | Default |
|---|---|---|
| `text` | Não | `""` (texto vazio busca tudo no intervalo) |
| `minDate` | Não | `1970-01-01 00:00:00 GMT` |
| `maxDate` | Não | Data/hora ATUAL **+ 24h** (para incluir o dia inteiro) |

**Engine:** `@Query` MongoDB com `$and` + `$or`:
```js
{
  $and: [
    { date: { $gte: minDate } },
    { date: { $lte: maxDate + 1 dia } },
    { $or: [
      { 'title':          { $regex: text, $options: 'i' } },
      { 'body':           { $regex: text, $options: 'i' } },
      { 'comments.text':  { $regex: text, $options: 'i' } }
    ] }
  ]
}
```

---

## 🚀 Como rodar o projeto

### 1. Pré-requisitos

- ☕ **JDK 21+** instalado (JAVA_HOME configurado)
- 🟢 **MongoDB** rodando localmente na porta `27017` (sem autenticação — config padrão)
- 📦 **Maven 3.9+** (ou use o wrapper `./mvnw`)

### 2. Clone / acesse o diretório

```bash
cd workshopmongo
```

### 3. Rodar a aplicação

```bash
mvn spring-boot:run
```

Ou via IDE: execute a classe principal [WorkshopmongoApplication.java](workshopmongo/src/main/java/com/project/workshopmongo/WorkshopmongoApplication.java)

### 4. Pronto! ✅

Ao iniciar, o `Instantiation` popula automaticamente:
- **3 Users:** Maria Brown, Alex Green, Bob Grey
- **2 Posts** da Maria com dados de exemplo

Acesse `http://localhost:8080/users` para confirmar.

### 5. Rodar testes

```bash
mvn test
```

---

## 🎓 Lições Aprendidas nesse projeto

### 1. **Diferença real de SQL vs NoSQL (MongoDB)**
Não existe JOIN nativo — aprendi a **desnormalizar e embedar DTOs** (`AuthorDTO` em `Post`, `CommentDTO` em `Post`) ao invés de criar relacionamentos estilo foreign key.

### 2. **Quando embedar vs quando referenciar com `@DBRef`**
- **Embeda:** quando os dados são acessados juntos (Post + seu Author + seus Comments).
- **Referencia (`@DBRef`):** quando a entidade é independente e só é carregada em casos específicos (`User.posts` com `lazy=true`).

### 3. **A importância do DTO na prática**
- **Segurança:** não expõe campos internos acidentalmente.
- **Evita Serialização Recursiva:** sem DTO, `User → Post → Author → User` entra em loop infinito no Jackson.
- **Consistência:** todos os endpoints retornam a mesma estrutura.

### 4. **`@Query` no Spring Data MongoDB é poderoso**
Aprendi a escrever **queries MongoDB nativas diretamente no Repository** para buscas complexas (`fullsearch` com `$and`, `$or`, `$regex`, comparação de datas) — sem precisar instanciar `MongoTemplate` manualmente.

### 5. **Boas práticas de Services com `Optional`**
O método `findById` do MongoRepository retorna `Optional<T>`. O pattern correto é:
```java
return obj.orElseThrow(() -> new ObjectNotFoundException("..."));
```
Nunca use `.get()` direto (deprecado) nem retorne `null`.

### 6. **`@ControllerAdvice` + Exception Customizada unifica erros**
Toda vez que um recurso não existe, basta lançar `ObjectNotFoundException` em QUALQUER service, e automaticamente a API responde `404 NOT_FOUND` com JSON padronizado (timestamp, status, error, message, path) — sem try/catch espalhado.

### 7. **Busca em parâmetros URL sempre deve ser decodificada**
Usuários digitam acentos, espaços, caracteres especiais. Sem `URLDecoder.decode(text, "UTF-8")` a busca por "São Paulo" falharia silenciosamente.

### 8. **Datas em buscas por intervalo são traiçoeiras**
Um dia no `maxDate` precisa ser **acrescentado 24h** em milissegundos. Porque `2018-12-31 00:00:00` pega só meia-noite do dia — posts das 10h daquele dia não seriam incluídos.

### 9. **`CommandLineRunner` para seed de dados em DEV**
Essa anotação `@Configuration` + `CommandLineRunner` roda código *toda vez* que o Spring sobe — perfeito para popular dados de teste. ⚠️ Mas é perigoso em produção (o `deleteAll()` apaga tudo), então aprendi que o ideal é usar `@Profile("dev")` no futuro.

### 10. **Java 21 + Spring Boot 4.x rodando do jeito certo**
Verifiquei na prática que a combinação é estável: Maven build, contextLoads do SpringBootTest, conexão do MongoDB driver 5.x, tudo 100% funcional sem nenhum warning bloqueante.

---

## ✅ Status de Validação

<p align="center">
  <table>
    <tr><td><code>mvn clean compile</code></td><td align="right">✅ BUILD SUCCESS</td></tr>
    <tr><td><code>mvn test</code></td><td align="right">✅ Tests run: 1 / Failures: 0 / Errors: 0</td></tr>
    <tr><td>Spring ApplicationContext</td><td align="right">✅ Inicia em ~2s</td></tr>
    <tr><td>Conexão MongoDB (localhost:27017)</td><td align="right">✅ Connected (STANDALONE)</td></tr>
    <tr><td>Repositórios detectados</td><td align="right">✅ 2 MongoDB Repository Interfaces</td></tr>
    <tr><td>Diagnósticos IDE</td><td align="right">✅ 0 issues</td></tr>
  </table>
</p>

---

<p align="center">
  <br>
  <strong>Feito com ❤️, muito código, ☕ e Spring Boot 4.</strong><br>
  <em>Primeiro passo para dominar persistência NoSQL e Web Services RESTful profissionais!</em>
</p>
