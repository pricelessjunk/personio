CREATE TABLE employeemgmt.supervisor (
  id BIGSERIAL PRIMARY KEY,
  id_employee BIGINT NOT NULL,
  id_supervisor BIGINT,
  CONSTRAINT fk_user_id FOREIGN KEY (id_employee) REFERENCES employeemgmt.employee(id),
  CONSTRAINT fk_supervisor_id FOREIGN KEY (id_supervisor) REFERENCES employeemgmt.employee(id)
);

INSERT INTO employeemgmt.employee(name) VALUES ('Nina');
INSERT INTO employeemgmt.employee(name) VALUES ('Elle');
INSERT INTO employeemgmt.supervisor(id_employee, id_supervisor) VALUES (1,2);