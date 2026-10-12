pipeline {
    agent-any
    parameters{
        choice(
        name:"ACTIVE_COLOR",
        choice:['blue','green'],
        description:'Which environment is serving traffic?')
    }
    environment{
        IMAGE_NAME="kneeharika27/blue-green-node"
    }
    stages{
        stage('Checkout'){
            steps{
                checkout scm
            }
        }
        stage("Build and Push Image"){
            steps{
                withCredentials([
                    usernamePassword(
                    credentialsId:'dockerhub-cred',
                    usernameVariable:'DOCKERHUB_USER'
                    passwordVariables:'DOCKERHUB_PASS'
                    )
                ])

                sh '''
                    echo "$DOCKERHUB_PASS" | docker login -u "$DOCKERHUB_USER" --pasword-stdin

                    docker build -t "$IMAGE_NAME:$BUILD_NUMBER" .
                    docker push "$IMAGE_NAME:$BUILD_NUMBER"
                '''

            }
        }

        stage("Deploy and Test new Environment"){
            steps{
                script{
                    def target=params.ACTIVE_COLOR=='blue'?'green':'blue'
                    env.TARGET_COLOR=target
                }
                sh '''
                    docker rm -f "$TARGET_COLOR" 2>/del/null ||true 
                    docker pull "$IMAGE_NAME:$BUILD_NUMBER"

                    docker run -d --name "$TARGET_COLOR" --network bgnet -e APP_VERSION="$BUILD_NUMBER" "$IMAGE_NAME:$BUILD_NUMBER"

                    docker rm --network bgnet curlimages/curl:latest -fsS "http://$TARGET_COLOR:3000"
                '''
            }
        }

        stage("Switch Traffic"){
            steps{
                sh '''
                    cat>nginx-new.conf<<EOF
                server{
                    listen 80;
                    location /{
                        proxy_pass http://$TARGET_COLOR:3000;
                        proxy_http_version 1.1;
                        proxy_set_header Host \\$host;
                        proxy_set_header X-Real-IP \\$remote_addr;
                        proxy_set_header X-Forwarded-For \\$proxy_add_x_forwarded_for;
                        proxy_set_header X-Forwarded-Proton \\$schema;

                    }
                }EOF

                docker cp nginx-new.conf nginx-proxy:/etc/nginx/conf.d/default.conf

                docker exec nginx-proxy nginx -t  
                docker exec nginx-proxy nginx -s reload 
                echo "Traffic shitched to $TARGET_COLOR "
                '''
            }

        }
    }
}