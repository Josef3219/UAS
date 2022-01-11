<?php
    require_once __DIR__ . '/koneksi.php';
    $db = new DB_CONNECT();

    $parameter = array(
        'id' => $_POST['id'],
        'namaproject' => $_POST['namaproject'],
        'pic' => $_POST['pic'],
        'startdate' => $_POST['startdate'],
        'enddate' => $_POST['enddate'],
        'action' => $_POST['action']
    );

    $response = array(
        'error' => FALSE,
        'error_text' => "",
        'peran' => "",
        'datanama' => array()
    );

    if($response['error'] == FALSE){
        switch($parameter['action']){
            case "add":
                if ($parameter['namaproject'] == "" || $parameter['pic'] == "" || $parameter['startdate'] == "" || $parameter['enddate'] == "" ) {
                    $response["error"] = TRUE;
                    $response["error_text"] = "Input tidak lengkap";
                } else {
                    // mengecek apakah project sudah terdaftar / belum
                    $query = "SELECT * FROM project WHERE namaproject = '".$parameter['namaproject']."'";
                    // mengupdate project yang dikerjakan oleh intern pada tabel intern berdasarkan PIC yang dipilih saat add project
                    $queryupdate = "UPDATE intern SET project = '".$parameter['namaproject']."' WHERE nama = '".$parameter['pic']."'";
                    $result = mysqli_query($db->connect(), $query);
                    $row = mysqli_num_rows($result);
                    if ($row == 0) {
                        mysqli_query($db->connect(), $queryupdate);
                        // menambah data ke tabel project berdasarkan data yang diinput kecuali enddate, enddate menggunakan '-' sebagai default pendaftaran project baru
                        $query = "INSERT INTO project (namaproject, pic,startdate, enddate) VALUES ('".$parameter['namaproject']."', '".$parameter['pic']."', '".$parameter['startdate']."', '-')";
                        mysqli_query($db->connect(), $query);
                        $response["error"] = FALSE;
                        $response["error_text"] = "Berhasil menambahkan Project"; 
                                   
                    } else {
                        $response["error"] = TRUE;
                        $response["error_text"] = "project sudah terdaftar";
                    }
                }
                $db->close();
                break;
            case "edit":
                // mengupdate tabel project berdasarkan id project
                $query = "UPDATE project SET
                `namaproject`='".$parameter['namaproject']."',
                `enddate`='".$parameter['enddate']."' WHERE id = '".$parameter['id']."'";
                // mengupdate tabel intern berdasarkan pic pada tabel project
                $queryupdate = "UPDATE intern SET project = '". $parameter['namaproject'] ."' WHERE nama = '".$parameter['pic']."'";
                if(mysqli_query($db->connect(), $query)) {
                    mysqli_query($db->connect(), $queryupdate);
                    $response["error"] = FALSE;
                    $response["error_text"] = "Berhasil mengubah data project";
                }
                else {
                    $response["error"] = TRUE;
                    $response["error_text"] = "Gagal mengubah data project";
                }
                $db->close();
                break;
            case "delete":
                // menghapus data project pada tabel project berdasarkan id project
                $query = "DELETE FROM project WHERE id = '".$parameter['id']."'";
                // mengupdate project yang dikerjakan oleh intern pada tabel intern berdasarkan pic pada tabel project
                $queryupdate = "UPDATE intern SET project = '-' WHERE nama = '".$parameter['pic']."'";
                if(mysqli_query($db->connect(), $query) && mysqli_query($db->connect(), $queryupdate)) {
                    $response["error"] = FALSE;
                    $response["error_text"] = "Berhasil hapus project";

                }
                else {
                    $response["error"] = TRUE;
                    $response["error_text"] = "Gagal hapus project";
                }
                 $db->close();
                break;
            case "fetchName":
                /* mengambil nama-nama intern yang tidak mempunyai project yang sedang dikerjakan atau ditandai dengan strip
                   untuk digunakan di spinner add project*/
                $query = "SELECT nama FROM intern WHERE project = '-'";
                $result = mysqli_query($db->connect(), $query);
                
                if (mysqli_num_rows($result) > 0) {
                    $hasil = array();
                    $urutan = 0;
                    // mengambil nama setiap intern dan memasukkannya ke variabel hasil
                    while ($col = mysqli_fetch_assoc($result)) {
                        $urutan = $urutan + 1;
                        $namaIntern = array(
                            'nama' => $col['nama']
                        );
                        $hasil[$urutan] = $namaIntern;
                    }
                    $response['error'] = FALSE;
                    $response['error_text'] = "Berhasil mengambil nama intern";
                    $response['datanama'] = $hasil;
                } else {
                    $response['error'] = TRUE;
                    $response['error_text'] = "Gagal mengambil nama intern";
                }
        }
    }
    echo json_encode($response, JSON_PRETTY_PRINT);
