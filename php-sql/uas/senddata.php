<?php
    require_once __DIR__ . '/koneksi.php';
    $db = new DB_CONNECT();

    $parameter = array(
        'username' => $_POST['username'],
        'email' => $_POST['email'],
        'peran' => $_POST['peran'],
        'password' => $_POST['password'],
        'newpass' => $_POST['newpassword'],
        'confirmnewpass' => $_POST['confirmnewpassword'],
        'action' => $_POST['action']
    );

    $response = array(
        'error' => FALSE,
        'error_text' => "",
        'peran' => "",
        "fullname" => ""
    );

    if($response['error'] == FALSE){
        switch($parameter['action']){
            case "register": 
                if ($parameter['username'] == "" || $parameter['email'] == "" || $parameter['password'] == "" ) {
                    $response["error"] = TRUE;
                    $response["error_text"] = "Input tidak lengkap";
                } else {
                    $query = "SELECT * FROM account WHERE username = '".$parameter['username']."'";
                    $result = mysqli_query($db->connect(), $query);
                    $row = mysqli_num_rows($result);
                    if ($parameter['peran'] == "user"){
                        $querycheckemail = "SELECT * FROM intern WHERE email = '".$parameter['email']."'";
                        // mengambil semua record dari table intern sesuai dengan email yang diinputkan
                    }
                    else {
                        $querycheckemail = "SELECT * FROM admin WHERE email = '".$parameter['email']."'";
                        // mengambil semua record dari table admin sesuai dengan email yang diinputkan

                    }
                    $resultcheckemail = mysqli_query($db->connect(), $querycheckemail);
                    $rowcheckemail = mysqli_num_rows($resultcheckemail);
                    if ($rowcheckemail != 0) {
                        if ($row == 0) {
                            $username_pattern = '/^((?!.*[^a-zA-Z\d])(?=.*[A-Z])(?=.*[a-z])(?=.*\d).{5,15})$/'; 
                            // pattern/syarat untuk penulisan username -> minimal ada 1 huruf besar, 1 huruf kecil, dan 1 angka, panjang karakter hanya 5 - 15 karakter
                            $password_pattern = '/^[\w]{5,15}$/';
                            /* pattern/syarat untuk penulisan password -> dapat terdiri atas huruf besar, huruf kecil, angka, 
                             dan underscore dengan batas karakter sebanyak 5 - 15 */
                            if(preg_match($username_pattern, $parameter['username'])){ 
                                if(preg_match($password_pattern, $parameter['password'])){
                                    $query = "INSERT INTO account (username, email, peran, password) VALUES ('".$parameter['username']."', '".$parameter['email']."', '".$parameter['peran']."', '".$parameter['password']."')";
                                    // menambah record ke table account 
                                    mysqli_query($db->connect(), $query);
                                    $response["error"] = FALSE;
                                    $response["error_text"] = "Berhasil registrasi";
                                }
                                else {
                                    $response["error"] = TRUE;
                                    $response["error_text"] = "Password hanya bisa Huruf, Angka, dan underscore, dan minimal 5 Karakter";
                                }
                            }
                            else {
                                $response["error"] = TRUE;
                                $response["error_text"] = "(Username)Minimal 1 angka, 1 huruf besar, dan 1 huruf kecil";
                            }
                        } else {
                            $response["error"] = TRUE;
                            $response["error_text"] = "Username telah terpakai";
                        }
                    }
                    else {
                        $response["error"] = TRUE;
                        $response["error_text"] = "Invalid email";
                    }
                }
                $db->close();
                break;
            case "login":
                if ($parameter['username'] == "" || $parameter['password'] == "" ) {
                    $response["error"] = TRUE;
                    $response["error_text"] = "Input tidak lengkap";
                } else {
                    $query = "SELECT * FROM account WHERE binary username='".$parameter['username']."' AND binary password='".$parameter['password']."'";                    
                    /* mengambil data dari table account berdasarkan usernama dan password 
                    binary -> case sensitive terhadap username dan password */
                    $result = mysqli_query($db->connect(), $query);
                    $row = mysqli_fetch_array($result);
                    if ($row > 0) {
                        $response["error"] = FALSE;
                        $response["error_text"] = "Berhasil login";
                    } else {
                        $response["error"] = TRUE;
                        $response["error_text"] = "Gagal login";
                    }
                    $result = mysqli_query($db->connect(), $query);
                    if (mysqli_num_rows($result) > 0) {
                        while ($col = mysqli_fetch_assoc($result)) {
                            $peranValue = $col['peran'];
                        }
                        $response['peran'] = $peranValue; // untuk memisahkan user dan admin
                    }
                    //mengambil nama dari intern yang login untuk ditampilkan di dalam aplikasi
                    $queryName = mysqli_query($db->connect(), "SELECT * FROM intern INNER JOIN account ON intern.email = account.email WHERE username = '".$parameter['username']."'");
                    if (mysqli_num_rows($queryName) > 0) {
                        while ($col = mysqli_fetch_assoc($queryName)) {
                            $nameValue = $col['nama'];
                        }
                        $response['fullname'] = $nameValue;
                    }

                }
                $db->close();
                break;
            case "change":
                $query = "SELECT password FROM account WHERE username='".$parameter['username']."'";
                // mengambil password dari table account berdasarkan usernama yang login ke dalam aplikasi 
                $result = mysqli_query($db->connect(), $query);
                $row = mysqli_fetch_array($result);
                $newpassword_pattern = '/^[\w]{5,15}$/';
                /* pattern/syarat untuk penulisan password -> dapat terdiri atas huruf besar, huruf kecil, angka, 
                dan underscore dengan batas karakter sebanyak 5 - 15 */
                if ($parameter['password'] != $row['password']) {
                    $response["error"] = TRUE;
                    $response['error_text'] = "Wrong Current Password";
                }
                else {
                    if($parameter['newpass'] == $parameter['confirmnewpass']){
                        if(preg_match($newpassword_pattern, $parameter['newpass'])){
                            if ($parameter['newpass'] != $parameter['password']) {
                                // mengupdate password di tabel account berdasarkan username yang login
                                $updatequery = "UPDATE account SET password= '".$parameter['newpass']."' WHERE username='".$parameter['username']."'";
                                mysqli_query($db->connect(), $updatequery);
                                $response["error"] = FALSE;
                                $response['error_text'] = "Berhasil ganti password";
                            }
                            else {
                                $response["error"] = TRUE;
                                $response['error_text'] = "Password lama dan baru tidak boleh sama";
                            }    
                        }
                        else {
                            $response["error"] = TRUE;
                            $response["error_text"] = "Password hanya bisa Huruf, Angka, dan underscore, dan minimal 5 Karakter";
                        }
                    }      
                    else {
                        $response["error"] = TRUE;
                        $response['error_text'] = "Password baru dan konfirmasi password baru tidak sama";
                    }              
                }      
                $db->close();
                break;
        }
    }
    
    echo json_encode($response);
    
