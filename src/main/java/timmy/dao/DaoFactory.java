package timmy.dao;

import timmy.dao.impl.*;
import timmy.utils.PropertiesUtil;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

@NoArgsConstructor
public class DaoFactory {

    private static DataSource source;
    private static DaoFactory daoFactory = new DaoFactory();

    public  static DaoFactory getInstance() { return daoFactory; }

    public  static DataSource getDataSource() {
        if (source == null) {
            source = getDriverManagerDataSource();
        }
        return source;
    }

    private static DriverManagerDataSource getDriverManagerDataSource() {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName(PropertiesUtil.getProperty("jdbc.driverClassName"));
        driver.setUrl(            PropertiesUtil.getProperty("jdbc.url"));
        driver.setUsername(       PropertiesUtil.getProperty("jdbc.username"));
        driver.setPassword(       PropertiesUtil.getProperty("jdbc.password"));
        return driver;
    }

    public PropertiesDao            getPropertiesDao() {            return new PropertiesDao(); }

    public LanguageUserDao          getLanguageUserDao() {          return new LanguageUserDao(); }

    public ButtonDao                getButtonDao() {                return new ButtonDao(); }

    public MessageDao               getMessageDao() {               return new MessageDao(); }

    public KeyboardMarkUpDao        getKeyboardMarkUpDao() {        return new KeyboardMarkUpDao(); }

    public UserDao                  getUserDao() {                  return new UserDao(); }

    public AdminDao                 getAdminDao() {                 return new AdminDao(); }

    public ServiceDao               getServiceDao() {               return new ServiceDao(); }

    public InvestmentsDao           getInvestmentsDao(){            return new InvestmentsDao(); }
    public GroupDao                 getGroupDao() {                     return new GroupDao();
    }

    public SuggestionDao           getSuggestionDao(){            return new SuggestionDao(); }
    public ComplaintDao           getComplaintDao(){            return new ComplaintDao(); }
    public OperatorsDao           getOperatorsDao(){            return new OperatorsDao(); }
    public ApplicationDao         getApplicationDao(){          return new ApplicationDao();}
    public PavilionDao         getPavilionDao(){          return new PavilionDao();}
    public QuestionDao          getQuestionDao(){           return new QuestionDao();}
    public QuestDao          getQuestDao(){           return new QuestDao();}
    public ReminderTaskDao          getReminderTaskDao(){           return new ReminderTaskDao();}




}
