<?php
include 'koneksi.php';

$id_user = $_POST['id_user'];
$nm_viewer = $_POST['nm_viewer'];

// Cek apakah user sudah pernah lihat produk ini
$cek = mysqli_query($conn, "SELECT * FROM tbl_viewer WHERE id_user='$id_user' AND nm_viewer='$nm_viewer'");
if (mysqli_num_rows($cek) > 0) {
    // Sudah pernah, tinggal update
    mysqli_query($conn, "UPDATE tbl_viewer SET total_viewer = total_viewer + 1 WHERE id_user='$id_user' AND nm_viewer='$nm_viewer'");
} else {
    // Belum pernah, insert baru
    mysqli_query($conn, "INSERT INTO tbl_viewer (id_user, nm_viewer, total_viewer) VALUES ('$id_user', '$nm_viewer', 1)");
}

$response = ["success" => true];
echo json_encode($response);
?>
