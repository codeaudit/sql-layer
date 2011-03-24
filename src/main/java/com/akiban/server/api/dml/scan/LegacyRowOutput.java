/**
 * Copyright (C) 2011 Akiban Technologies Inc.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 */

package com.akiban.server.api.dml.scan;

import com.akiban.server.RowData;

import java.nio.ByteBuffer;

public interface LegacyRowOutput {
    ByteBuffer getOutputBuffer() throws RowOutputException;

    /**
     * Signals that a row has been written into the buffer. It could be that the row actually caused the scan's
     * limit to be exceeded, and that it should actually be disregarded; if so, that is communicated via
     * limitExceeded
     * @param limitExceeded whether the row that was written was actually in excess of the limit
     * @throws RowOutputException if thrown during any processing that happens as a result of this method
     */
    void wroteRow(boolean limitExceeded) throws RowOutputException;

    void addRow(RowData rowData) throws RowOutputException;

    int getRowsCount();

    boolean getOutputToMessage();
}
