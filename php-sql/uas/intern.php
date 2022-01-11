<?php
    require_once __DIR__ . '/koneksi.php';
    $db = new DB_CONNECT();

    $parameter = array(
        'id' => $_POST['id'],
        'nama' => $_POST['nama'],
        'umur' => $_POST['umur'],
        'jenkel' => $_POST['jenkel'],
        'email' => $_POST['email'],
        'alamat' => $_POST['alamat'],
        'telpon' => $_POST['telpon'],
        'performa' => $_POST['performa'],
        'project' => $_POST['project'],
        'action' => $_POST['action']
    );

    $response = array(
        'error' => FALSE,
        'error_text' => "",
        'peran' => ""
    );

    if($response['error'] == FALSE){
        switch($parameter['action']){
            case "add":
                if ($parameter['nama'] == "" || $parameter['umur'] == "" || $parameter['jenkel'] == "" || $parameter['email'] == "" || $parameter['alamat'] == "" || $parameter['telpon'] == "" || $parameter['performa'] == "" ) {
                    $response["error"] = TRUE;
                    $response["error_text"] = "Input tidak lengkap";
                } else {
                    // mengecek nama apakah nama sudah atau belum di tabel intern
                    $query = "SELECT * FROM intern WHERE nama = '".$parameter['nama']."'";
                    $result = mysqli_query($db->connect(), $query);
                    $row = mysqli_num_rows($result);
                    if ($row == 0) {
                        $name_pattern = '/^[a-zA-Z ]*$/';
                        // syarat penulisan nama -> hanya terdiri dari huruf besar, huruf kecil, dan spasi 
                        $email_pattern = '/^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,3})$/';
                        // syarat penulisan email -> dapat berupa angka, huruf, underscore, titik, @
                        $telpon_pattern = '/^[\d]{11,15}$/';
                        // syarat penulisan telpon -> berupa angka panjang karakter 11-15 karakter
                        if(preg_match($name_pattern, $parameter['nama'])){ 
                            if(preg_match($email_pattern, $parameter['email'])){
                                if(preg_match($telpon_pattern, $parameter['telpon'])){
                                    $query = "INSERT INTO intern (nama, umur, jenkel, email, alamat, telpon, gambar, performa, project) VALUES ('".$parameter['nama']."', '".$parameter['umur']."', '".$parameter['jenkel']."', '".$parameter['email']."', '".$parameter['alamat']."', '".$parameter['telpon']."', 'https://i.ibb.co/t3snVGM/user.png' ,'".$parameter['performa']."', '".$parameter['project']."')";
                                    // menambah data ke dalam table intern dengan data yang diinputkan, kecuali untuk gambar, gambar menggunakan link default
                                    mysqli_query($db->connect(), $query);
                                    $response["error"] = FALSE;
                                    $response["error_text"] = "Berhasil menambahkan intern";
                                }
                                else{
                                    $response["error"] = TRUE;
                                    $response["error_text"] = "No telpon kurang dari 11 karakter atau lebih dari 15 karakter";
                                }
                            }
                            else{
                                $response["error"] = TRUE;
                                $response["error_text"] = "format email salah";
                            }
                        }
                        else {
                            $response["error"] = TRUE;
                            $response["error_text"] = "Nama hanya huruf besar dan huruf kecil";
                        }
                    } else {
                        $response["error"] = TRUE;
                        $response["error_text"] = "Intern sudah terdaftar";
                    }
                }
                $db->close();
                break;
            case "edit":
                // mengupdate data intern pada table intern berdasarkan id intern
                $query = "UPDATE intern SET
                `nama`='".$parameter['nama']."',
                `umur`='".$parameter['umur']."',
                `alamat`='".$parameter['alamat']."',
                `telpon`='".$parameter['telpon']."',
                `performa`='".$parameter['performa']."' WHERE id = '".$parameter['id']."'";
                if(mysqli_query($db->connect(), $query)) {
                    $response["error"] = FALSE;
                    $response["error_text"] = "Berhasil mengubah data intern";
                }
                else {
                    $response["error"] = TRUE;
                    $response["error_text"] = "Gagal mengubah data intern";
                }
                    $db->close();
                break;
            case "delete":
                // menghapus intern dari tabel intern
                $query = "DELETE FROM intern WHERE id = '".$parameter['id']."'";
                if(mysqli_query($db->connect(), $query)) {
                    $response["error"] = FALSE;
                    $response["error_text"] = "Berhasil hapus intern";
                }
                else {
                    $response["error"] = TRUE;
                    $response["error_text"] = "Gagal hapus intern";
                }
                 $db->close();
                break;

        }
    }
    echo json_encode($response);
