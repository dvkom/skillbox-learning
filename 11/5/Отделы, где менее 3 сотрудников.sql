SELECT d.name FROM learn.employee e
JOIN learn.department d ON d.id=e.department_id
GROUP BY d.id
HAVING COUNT(e.id)<3