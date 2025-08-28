@Configuration
public class DataSourceConfig {

    @Profile("prod")
    @Configuration
    static class ProductionDataSourceConfig {

        @Value("${DB_URL}")
        private String dbUrl;

        @Value("${DB_USERNAME}")
        private String dbUsername;

        @Value("${DB_SECRET_NAME}")
        private String secretName;

        private final SecretsManagerService secretsService;

        public ProductionDataSourceConfig(SecretsManagerService secretsService) {
            this.secretsService = secretsService;
        }

        @Bean
        public DataSource dataSource() {
            String dbPassword = secretsService.getSecret(secretName);

            return DataSourceBuilder.create()
                    .url(dbUrl)
                    .username(dbUsername)
                    .password(dbPassword)
                    .driverClassName("com.mysql.cj.jdbc.Driver")
                    .build();
        }
    }
}
