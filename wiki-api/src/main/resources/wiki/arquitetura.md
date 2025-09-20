# Arquitetura da Solução


# Arquitetura da Solução

A arquitetura do sistema segue um modelo cliente-servidor desacoplado, onde o frontend e backend se comunicam através de uma API RESTful.

## Diagrama de Arquitetura

![Diagrama de Arquitetura](../diagrama_arquitetura.png)

## Arquitetura do Backend (Spring Boot)

O backend é estruturado em camadas bem definidas:

### Camada de Apresentação
- **Controllers:** Exposição de endpoints REST
- **DTOs:** Objetos de transferência de dados
- **Exception Handlers:** Tratamento centralizado de erros
- **Security Config:** Configurações de segurança

### Camada de Negócio
- **Services:** Lógica de negócio principal
- **Validations:** Regras de validação
- **Business Rules:** Regras específicas do domínio

### Camada de Persistência
- **Repositories:** Acesso a dados via JPA
- **Entities:** Mapeamento objeto-relacional
- **Database:** PostgreSQL/MySQL

## Estrutura de Pacotes

```
src/main/java/com/joao/osMarmoraria/
├── config/              # Configurações da aplicação
├── controle/            # Controllers REST
├── domain/              # Entidades de domínio
├── dtos/                # Data Transfer Objects
├── repository/          # Repositórios JPA
├── security/            # Configurações de segurança
├── services/            # Lógica de negócio
└── OsMarmorariaApplication.java
```

## Arquitetura do Frontend (Angular)

### Estrutura Modular

```
src/app/
├── core/                # Serviços singleton
├── shared/              # Componentes reutilizáveis
├── modules/             # Módulos de funcionalidades
│   ├── clientes/
│   ├── produtos/
│   ├── vendas/
│   └── ...
├── guards/              # Guards de rota
├── services/            # Serviços HTTP
└── models/              # Interfaces TypeScript
```

### Padrões Arquiteturais

#### Lazy Loading
Módulos carregados sob demanda para melhor performance:

```typescript
{
  path: 'clientes',
  loadChildren: () => import('./modules/clientes/clientes.module')
    .then(m => m.ClientesModule)
}
```

#### Reactive Programming
Uso extensivo de RxJS para gerenciamento de estado:

```typescript
getClientes(): Observable<Cliente[]> {
  return this.http.get<Cliente[]>('/api/clientes')
    .pipe(
      catchError(this.handleError),
      shareReplay(1)
    );
}
```

## Comunicação Frontend-Backend

### Autenticação JWT
1. Login com credenciais
2. Recebimento do token JWT
3. Inclusão do token em todas as requisições
4. Validação no backend via Spring Security

### Interceptors HTTP
Interceptação automática para:
- Inclusão de tokens de autenticação
- Tratamento de erros globais
- Loading states
- Retry automático

## Segurança

### Backend Security
- **Spring Security** para autenticação/autorização
- **JWT tokens** para sessões stateless
- **CORS** configurado para frontend
- **Validação** de entrada em todos os endpoints

### Frontend Security
- **Route Guards** para proteção de rotas
- **Token storage** seguro
- **Automatic logout** em caso de token expirado
- **Input sanitization** para XSS prevention

## Escalabilidade e Performance

### Backend
- **Connection pooling** para banco de dados
- **Lazy loading** de relacionamentos JPA
- **Caching** com Spring Cache
- **Pagination** para grandes datasets

### Frontend
- **Lazy loading** de módulos
- **OnPush** change detection strategy
- **Virtual scrolling** para listas grandes
- **Service workers** para cache offline

## Monitoramento e Logs

### Backend Monitoring
- **Spring Boot Actuator** para health checks
- **Logback** para logging estruturado
- **Micrometer** para métricas
- **Exception tracking** centralizado

### Frontend Monitoring
- **Error boundary** para captura de erros
- **Performance monitoring** com Angular DevTools
- **User analytics** para UX insights
                