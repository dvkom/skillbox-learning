SELECT d.name, COUNT(e.id) FROM learn.employee e
JOIN learn.department d ON d.id=e.department_id
GROUP BY d.id