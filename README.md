# campus-order

A personal microservices-based project focused on campus trading scenarios.

## Core capabilities

- **Product listing**: publish and browse second-hand products on campus.
- **Order management**: create, pay, track, and complete campus trade orders.
- **Message notification**: notify buyers and sellers about order and product events.
- **Permission control**: role-based access for users, merchants, and administrators.
- **AI-powered product summary**: generate concise product highlights to improve listing quality.
- **High-concurrency flash sale**: support spike traffic with inventory protection and fast order flow.

## Suggested microservice split

- `product-service`: listing, search, product details, and inventory basics.
- `order-service`: order lifecycle, status changes, and transaction orchestration.
- `message-service`: in-app/system notifications and asynchronous event delivery.
- `auth-service`: login, identity, role/permission checks, token validation.
- `ai-service`: product content analysis and summary generation APIs.
- `seckill-service`: flash sale queueing, stock deduction, anti-oversell controls.
