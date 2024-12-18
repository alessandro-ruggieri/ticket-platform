## Database: db_ticket

# Table: users

- id INT PK AI UNIQUE NOT NULL
- email VARCHAR(100) UNIQUE NOT NULL
- password VARCHAR(60) NOT NULL
- name VARCHAR(60) NOT NULL
- surname VARCHAR(60) NOT NULL
- role_id INT FK NOT NULL
- status ENUM('active', 'inactive') DEFAULT 'active' NOT NULL

# Table: roles

- id SMALLINT PK AI UNIQUE NOT NULL
- name VARCHAR(50) UNIQUE NOT NULL

# Table: tickets

- id BIGINT PK AI UNIQUE NOT NULL
- title VARCHAR(255) NOT NULL
- description TEXT NOT NULL
- status ENUM('todo', 'in_progress', 'completed') DEFAULT 'todo' NOT NULL
- category_id INT FK NOT NULL
- operator_id INT FK NOT NULL
- created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL

# Table: categories

- id SMALLINT PK AI UNIQUE NOT NULL
- name VARCHAR(100) UNIQUE NOT NULL

# Table: notes

- id BIGINT PK AI UNIQUE NOT NULL
- ticket_id BIGINT FK NOT NULL
- author_id INT FK NOT NULL
- content TEXT NOT NULL
- created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL
