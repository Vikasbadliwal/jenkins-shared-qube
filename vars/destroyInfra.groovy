def call() {
    echo " Initiating Infrastructure Teardown..."
    sh 'terraform init'
    sh 'terraform destroy -auto-approve'
    echo " Infrastructure successfully destroyed. No more AWS charges!"
}
