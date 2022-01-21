CREATE TABLE employeemgmt.supervisor (
  id BIGSERIAL PRIMARY KEY,
  id_employee BIGINT NOT NULL,
  id_supervisor BIGINT,
  CONSTRAINT fk_user_id FOREIGN KEY (id_employee) REFERENCES employeemgmt.employee(id),
  CONSTRAINT fk_supervisor_id FOREIGN KEY (id_supervisor) REFERENCES employeemgmt.employee(id)
);