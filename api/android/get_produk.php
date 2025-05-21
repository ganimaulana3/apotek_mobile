<?php
include "koneksi.php";
header("Content-Type: application/json");

$sql = "SELECT nm_produk, harga, stok, img_produk FROM tbl_produk";
$result = $conn->query($sql);

$products = [];
while ($row = $result->fetch_assoc()) {
    $products[] = $row;
}

echo json_encode($products);
$conn->close();

?>