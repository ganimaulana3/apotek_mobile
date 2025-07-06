<?php
include 'koneksi.php';
header("Content-Type: application/json");

// SQL JOIN to merge produk with viewer info
$query = mysqli_query($conn, "
    SELECT 
        p.id_produk,
        p.nm_produk,
        p.harga,
        p.stok,
        p.img_produk,
        IFNULL(SUM(v.total_viewer), 0) AS total_views
    FROM produk p
    LEFT JOIN tbl_viewer v ON p.id_produk = v.id_produk
    GROUP BY p.id_produk, p.nm_produk, p.harga, p.stok, p.img_produk
");

$data = [];

while($row = mysqli_fetch_assoc($query)) {
    $data[] = $row;
}

echo json_encode($data);
?>
