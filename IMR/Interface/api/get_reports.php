<?php
header('Content-Type: application/json; charset=utf-8');

$report = isset($_GET['report']) ? $_GET['report'] : '';
$start = isset($_GET['start']) && $_GET['start'] !== '' ? $_GET['start'] : null;
$end = isset($_GET['end']) && $_GET['end'] !== '' ? $_GET['end'] : null;

// DB credentials - adjust for your environment. With XAMPP default: root / no password
$dbHost = '127.0.0.1';
$dbUser = 'root';
$dbPass = '';
$dbName = 'ggcu';

$mysqli = new mysqli($dbHost, $dbUser, $dbPass, $dbName);
if ($mysqli->connect_errno) {
  echo json_encode(['success'=>false,'error'=>'DB connection failed']);
  exit;
}

if ($report === 'revenue') {
  // Sum payments grouped by utility between optional dates
  $params = [];
  $sql = "SELECT ui.name AS utility, SUM(p.amount) AS revenue
    FROM payments p
    JOIN bill_items bi ON bi.bill_id = p.bill_id
    JOIN utilities ui ON ui.id = bi.utility_id";

  if ($start && $end) {
    $sql .= " WHERE p.payment_date BETWEEN ? AND ?";
    $params[] = $start;
    $params[] = $end;
  }
  $sql .= " GROUP BY ui.id ORDER BY revenue DESC";

  if ($stmt = $mysqli->prepare($sql)) {
    if (count($params) === 2) $stmt->bind_param('ss', $params[0], $params[1]);
    $stmt->execute();
    $res = $stmt->get_result();
    $rows = [];
    while ($r = $res->fetch_assoc()) $rows[] = $r;
    echo json_encode(['success'=>true,'rows'=>$rows]);
    $stmt->close();
    $mysqli->close();
    exit;
  }
  echo json_encode(['success'=>false,'error'=>'Query prepare failed']);
  $mysqli->close();
  exit;
}

echo json_encode(['success'=>false,'error'=>'Unknown report']);

?>
