/**
 * END USER LICENSE AGREEMENT (“EULA”)
 *
 * READ THIS AGREEMENT CAREFULLY (date: 9/13/2011):
 * http://www.akiban.com/licensing/20110913
 *
 * BY INSTALLING OR USING ALL OR ANY PORTION OF THE SOFTWARE, YOU ARE ACCEPTING
 * ALL OF THE TERMS AND CONDITIONS OF THIS AGREEMENT. YOU AGREE THAT THIS
 * AGREEMENT IS ENFORCEABLE LIKE ANY WRITTEN AGREEMENT SIGNED BY YOU.
 *
 * IF YOU HAVE PAID A LICENSE FEE FOR USE OF THE SOFTWARE AND DO NOT AGREE TO
 * THESE TERMS, YOU MAY RETURN THE SOFTWARE FOR A FULL REFUND PROVIDED YOU (A) DO
 * NOT USE THE SOFTWARE AND (B) RETURN THE SOFTWARE WITHIN THIRTY (30) DAYS OF
 * YOUR INITIAL PURCHASE.
 *
 * IF YOU WISH TO USE THE SOFTWARE AS AN EMPLOYEE, CONTRACTOR, OR AGENT OF A
 * CORPORATION, PARTNERSHIP OR SIMILAR ENTITY, THEN YOU MUST BE AUTHORIZED TO SIGN
 * FOR AND BIND THE ENTITY IN ORDER TO ACCEPT THE TERMS OF THIS AGREEMENT. THE
 * LICENSES GRANTED UNDER THIS AGREEMENT ARE EXPRESSLY CONDITIONED UPON ACCEPTANCE
 * BY SUCH AUTHORIZED PERSONNEL.
 *
 * IF YOU HAVE ENTERED INTO A SEPARATE WRITTEN LICENSE AGREEMENT WITH AKIBAN FOR
 * USE OF THE SOFTWARE, THE TERMS AND CONDITIONS OF SUCH OTHER AGREEMENT SHALL
 * PREVAIL OVER ANY CONFLICTING TERMS OR CONDITIONS IN THIS AGREEMENT.
 */

package com.akiban.server.types3.common;

import com.akiban.server.rowdata.ConversionHelperBigDecimal;
import com.akiban.server.types3.TClassFormatter;
import com.akiban.server.types3.TInstance;
import com.akiban.server.types3.common.types.StringAttribute;
import com.akiban.server.types3.common.types.StringFactory;
import com.akiban.server.types3.mcompat.mtypes.MBigDecimal.Attrs;
import com.akiban.server.types3.pvalue.PValueSource;
import com.akiban.util.AkibanAppender;
import com.google.common.primitives.UnsignedLongs;

import java.math.BigDecimal;
import java.nio.charset.Charset;

public class NumericFormatter {

    public static enum FORMAT implements TClassFormatter {
        FLOAT {
            @Override
            public void format(TInstance instance, PValueSource source, AkibanAppender out) {
                out.append(Float.toString(source.getFloat()));
            }
        },
        DOUBLE {
            @Override
            public void format(TInstance instance, PValueSource source, AkibanAppender out) {
                out.append(Double.toString(source.getDouble()));
            }
        },
        INT_8 {
            @Override
            public void format(TInstance instance, PValueSource source, AkibanAppender out) {
                out.append(Byte.toString(source.getInt8()));
            }
        },
        INT_16 {
            @Override
            public void format(TInstance instance, PValueSource source, AkibanAppender out) {
                out.append(Short.toString(source.getInt16()));
            }
        },
        INT_32 {
            @Override
            public void format(TInstance instance, PValueSource source, AkibanAppender out) {
                out.append(Integer.toString(source.getInt32()));
            }
        },
        INT_64 {
            @Override
            public void format(TInstance instance, PValueSource source, AkibanAppender out) {
                out.append(Long.toString(source.getInt64()));
            }
        },
        UINT_64 {
            @Override
            public void format(TInstance instance, PValueSource source, AkibanAppender out) {
                out.append(UnsignedLongs.toString(source.getInt64()));
            }
        },
        BYTES {
            @Override
            public void format(TInstance instance, PValueSource source, AkibanAppender out) {
                int charsetId = instance.attribute(StringAttribute.CHARSET);
                String charsetName = (StringFactory.Charset.values())[charsetId].name();
                
                Charset charset = Charset.forName(charsetName);
                out.append(new String(source.getBytes(), charset));
            }
        },BIGDECIMAL{
            @Override
            public void format(TInstance instance, PValueSource source, AkibanAppender out) {
                if (source.hasCacheValue()) {
                    BigDecimal num = ((BigDecimalWrapper) source.getObject()).asBigDecimal();
                    out.append(num.toString());
                }
                else {
                    int precision = instance.attribute(Attrs.PRECISION);
                    int scale = instance.attribute(Attrs.SCALE);
                    ConversionHelperBigDecimal.decodeToString(source.getBytes(), 0, precision, scale, out);
                }
            }
        }
    }
}
