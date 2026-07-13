# notification-service

Serviço de notificações do Rota Fácil. Consome eventos RabbitMQ, envia e-mails HTML e persiste notificações consultáveis pelos usuários autenticados.

## Responsabilidades

- Enviar e-mail de boas-vindas e de conta removida.
- Avisar passageiros sobre início e cancelamento de viagens.
- Enviar e-mail quando um usuário recebe feedback.
- Persistir notificações de viagem iniciada e cancelada.
- Expor a caixa de notificações do usuário pelo gateway.

## Porta e base path

- Porta: `8086`
- Context path: `/notifications`
- Via gateway: `http://localhost:8080/notifications`

## API

- `GET /notifications/my?page=0&size=20&sort=createdAt,desc`: retorna uma página de notificações do usuário autenticado.
- `GET /notifications/v3/api-docs`
- `/notifications/swagger-ui.html`

A identidade é obtida dos headers `x-user-*` injetados pelo gateway. A consulta retorna somente notificações do usuário autenticado.

## Eventos consumidos

| Exchange | Routing key | Fila padrão | Efeito |
| --- | --- | --- | --- |
| `auth.events` | `user.created` | `notification.user.created.queue` | Envia e-mail de boas-vindas. |
| `auth.events` | `user.deleted` | `notification.user.deleted.queue` | Envia e-mail de conta removida. |
| `transport.events` | `trip.running` | `notification.trip.running.queue` | Persiste notificações de viagem iniciada. |
| `transport.events` | `trip.cancelled` | `notification.trip.cancelled.queue` | Envia e-mails e persiste notificações de cancelamento. |
| `transport.events` | `user.feedback` | `notification.user.feedback.queue` | Envia e-mail com feedback e nota. |

Os handlers de viagem registram falhas no log e encerram o processamento. O listener usa `defaultRequeueRejected=false`, evitando reprocessamento infinito de mensagens inválidas. Essa política descarta a mensagem após a falha; uma recuperação posterior exigirá DLQ.

## Templates

Os templates ficam em `src/main/resources/templates/emails`: `created-account.html`, `deleted-account.html`, `trip-cancelled.html` e `feedback-received.html`.

## Persistência

- Banco padrão: `jdbc:postgresql://localhost:5438/notification_database`
- Usuário padrão: `rota-facil`
- Migrations: `src/main/resources/db/migration`
- Hibernate: `ddl-auto=validate`

## E-mail local

Por padrão o serviço usa MailHog em `localhost:1025`; a interface fica em `http://localhost:8025`.

## Como rodar

Pré-requisitos: Java 21, PostgreSQL, Eureka, RabbitMQ e SMTP/MailHog.

```bash
cd notification-service
docker compose up -d
./mvnw spring-boot:run
```

Variáveis principais: `NOTIFICATION_DATASOURCE_URL`, `DATASOURCE_USERNAME`, `DATASOURCE_PASSWORD`, `EUREKA_URL`, `RABBITMQ_*`, `MAIL_HOG_HOST` e, em produção, as propriedades SMTP.

## Limite do domínio

O serviço transforma eventos em comunicação e histórico consultável. Regras transacionais permanecem nos serviços de origem.
