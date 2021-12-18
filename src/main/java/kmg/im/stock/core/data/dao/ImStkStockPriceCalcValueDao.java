package kmg.im.stock.core.data.dao;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import kmg.core.domain.model.SqlPathModel;
import kmg.core.domain.model.impl.SqlPathModelImpl;
import kmg.core.infrastructure.exception.KmgDomainException;
import kmg.core.infrastructure.type.KmgString;
import kmg.im.stock.core.data.dto.SpcvDeleteCondDto;
import kmg.im.stock.core.data.dto.StockPriceCalcValueDto;
import kmg.im.stock.core.data.dto.impl.SpcvDeleteCondDtoImpl;
import kmg.im.stock.core.infrastructure.types.ImStkPeriodTypeTypes;

/**
 * 投資株式株価計算値ＤＡＯ<br>
 *
 * @author KenichiroArai
 * @sine 1.0.0
 * @version 1.0.0
 */
@Repository
@SuppressWarnings("nls")
public class ImStkStockPriceCalcValueDao {

    /** 私自身のクラス */
    private static final Class<?> MYSELF_CLASS = ImStkStockPriceCalcValueDao.class;

    /** 株価銘柄ＩＤと投資株式期間の種類の種類に該当するデータを削除するＳＱＬパス */
    private static final SqlPathModel DELETE_BY_SB_ID_AND_PERIOD_TYPE_TYPES_SQL_PATH = new SqlPathModelImpl(
        ImStkStockPriceCalcValueDao.MYSELF_CLASS, Paths.get("deleteBySbIdAndImStkPeriodTypeTypes.sql"));

    /** 株価計算値を挿入するＳＱＬパス */
    private static final SqlPathModel INSERT_SQL_PATH = new SqlPathModelImpl(ImStkStockPriceCalcValueDao.MYSELF_CLASS,
        Paths.get("insert.sql"));

    /** データベース接続 */
    private final NamedParameterJdbcTemplate jdbc;

    /**
     * コンストラクタ<br>
     *
     * @author KenichiroArai
     * @sine 1.0.0
     * @version 1.0.0
     * @param jdbc
     *             データベース接続
     */
    public ImStkStockPriceCalcValueDao(final NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * 株価銘柄ＩＤと投資株式期間の種類の種類に該当するデータを削除する<br>
     *
     * @author KenichiroArai
     * @sine 1.0.0
     * @version 1.0.0
     * @param sbId
     *                        株価銘柄ＩＤ
     * @param periodTypeTypes
     *                        投資株式期間の種類の種類
     * @return 削除数
     * @throws KmgDomainException
     *                            ＫＭＧドメイン例外
     */
    public long deleteByIdCdAndImStkPeriodTypeTypes(final long sbId, final ImStkPeriodTypeTypes periodTypeTypes)
        throws KmgDomainException {

        long result = 0L;

        /* パラメータを設定する */
        final SpcvDeleteCondDto spcvDeleteCondDto = new SpcvDeleteCondDtoImpl();
        spcvDeleteCondDto.setStockBrandId(sbId);
        spcvDeleteCondDto.setPeriodTypeId(periodTypeTypes.get());
        final SqlParameterSource param = new BeanPropertySqlParameterSource(spcvDeleteCondDto);

        /* SQLを作成する */
        final String sql = ImStkStockPriceCalcValueDao.DELETE_BY_SB_ID_AND_PERIOD_TYPE_TYPES_SQL_PATH.toSql();

        /* 削除する */
        result = this.jdbc.update(sql, param);

        return result;
    }

    /**
     * 株価計算値を挿入する<br>
     *
     * @author KenichiroArai
     * @sine 1.0.0
     * @version 1.0.0
     * @param stockPriceCalcValueDto
     *                               株価計算値ＤＴＯ
     * @throws KmgDomainException
     *                            ＫＭＧドメイン例外
     * @return 挿入数
     */
    public long insert(final StockPriceCalcValueDto stockPriceCalcValueDto) throws KmgDomainException {

        long result = 0L;

        /* パラメータを設定する */
        stockPriceCalcValueDto.setStartDate(LocalDate.MIN);
        stockPriceCalcValueDto.setEndDate(LocalDate.MAX);
        stockPriceCalcValueDto.setLocaleId("ja"); // TODO KenichiroArai 2021/05/01 列挙型
        stockPriceCalcValueDto.setCreator("TSSTS_MAIN_USER"); // TODO KenichiroArai 2021/06/27 後で考える
        stockPriceCalcValueDto.setCreatedDate(LocalDateTime.now());
        stockPriceCalcValueDto.setUpdater("TSSTS_MAIN_USER"); // TODO KenichiroArai 2021/06/27 後で考える
        stockPriceCalcValueDto.setUpdateDate(LocalDateTime.now());
        stockPriceCalcValueDto.setNote(KmgString.EMPTY);
        stockPriceCalcValueDto.setName(KmgString.EMPTY);

        final SqlParameterSource param = new BeanPropertySqlParameterSource(stockPriceCalcValueDto);
        result = this.jdbc.update(ImStkStockPriceCalcValueDao.INSERT_SQL_PATH.toSql(), param);

        return result;

    }

}