<?php
include 'koneksi.php';
header("Content-Type: application/json");

if (isset($_GET['id_produk'])) {
    $id = $_GET['id_produk'];
    $query = mysqli_query($conn, "SELECT * FROM produk WHERE id_produk = '$id'");
    $data = mysqli_fetch_assoc($query);
    echo json_encode($data);
}
?>
