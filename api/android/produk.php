<?php
include 'koneksi.php';
header("Content-Type: application/json");

$query = mysqli_query($conn, "SELECT * FROM produk");
$data = [];

while($row = mysqli_fetch_assoc($query)) {
    $data[] = $row;
}

echo json_encode($data);
?>