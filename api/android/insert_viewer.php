<?php
include 'koneksi.php';

$id_user = $_POST['id_user'];
$id_produk = $_POST['id_produk'];
$nm_viewer = $_POST['nm_viewer'];


// Cek apakah user sudah pernah lihat produk ini
$cek = mysqli_query($conn, "SELECT * FROM tbl_viewer WHERE id_user='$id_user' AND id_produk='$id_produk'");
if (mysqli_num_rows($cek) > 0) {
    // Sudah pernah, tinggal update
    mysqli_query($conn, "UPDATE tbl_viewer SET total_viewer = total_viewer + 1 WHERE id_user='$id_user' AND id_produk='$id_produk'");
} else {
    // Belum pernah, insert baru
    mysqli_query($conn, "INSERT INTO tbl_viewer (id_user, id_produk, nm_viewer, total_viewer) VALUES ('$id_user', '$id_produk', '$nm_viewer', 1)");
}

$response = ["success" => true];
echo json_encode($response);
?>
