uniform highp mat4 matProj;
uniform highp vec4 screenSize;
uniform highp mat4 matWorldView;
uniform highp mat4 matWorldViewProj;
uniform highp mat4 matDataTransform;
uniform highp vec4 PenThickness;
uniform highp vec4 PenColor;
attribute highp vec3 vTexCoord0;
attribute highp vec4 vColor1;
attribute highp vec3 vTexCoord1;
attribute highp vec4 vColor2;
attribute highp vec4 vTexCoord4;
varying highp vec4 xlv_COLOR0;
varying highp vec3 xlv_TEXCOORD0;
varying highp vec2 xlv_TEXCOORD1;
void main ()
{
  highp vec4 tmpvar_1;
  highp mat4 tmpvar_2;
  tmpvar_2 = (matWorldView * matProj);
  highp vec3 tmpvar_3;
  highp vec3 tmpvar_4;
  tmpvar_3.z = vTexCoord0.z;
  tmpvar_4.z = vTexCoord1.z;
  highp vec3 tmpvar_5;
  highp vec3 tmpvar_6;
  highp vec3 tmpvar_7;
  highp vec4 tmpvar_8;
  highp float tmpvar_9;
  tmpvar_9 = vTexCoord4.x;
  highp float tmpvar_10;
  tmpvar_10 = PenThickness.x;
  highp vec4 tmpvar_11;
  tmpvar_11.xy = vTexCoord0.xy;
  tmpvar_11.zw = vTexCoord1.xy;
  highp vec4 tmpvar_12;
  highp vec4 tmpvar_13;
  tmpvar_13.x = matDataTransform[0].x;
  tmpvar_13.y = matDataTransform[1].y;
  tmpvar_13.z = matDataTransform[0].x;
  tmpvar_13.w = matDataTransform[1].y;
  highp vec4 tmpvar_14;
  tmpvar_14.x = matDataTransform[0].w;
  tmpvar_14.y = matDataTransform[1].w;
  tmpvar_14.z = matDataTransform[0].w;
  tmpvar_14.w = matDataTransform[1].w;
  tmpvar_12 = ((tmpvar_11 * tmpvar_13) + tmpvar_14);
  tmpvar_3.xy = tmpvar_12.xy;
  tmpvar_4.xy = tmpvar_12.zw;
  highp float tmpvar_15;
  tmpvar_15 = abs(vTexCoord4.w);
  if ((tmpvar_15 == 0.0)) {
    tmpvar_4.y = tmpvar_3.y;
    highp float tmpvar_16;
    tmpvar_16 = (0.5 * PenThickness.x);
    tmpvar_4.x = (tmpvar_12.z - (tmpvar_16 * sign(
      (tmpvar_12.z - tmpvar_12.x)
    )));
    tmpvar_3.x = (tmpvar_12.x - (tmpvar_16 * sign(
      (tmpvar_4.x - tmpvar_12.x)
    )));
  } else {
    tmpvar_3.x = tmpvar_4.x;
    highp float tmpvar_17;
    tmpvar_17 = (0.5 * PenThickness.x);
    tmpvar_4.y = (tmpvar_4.y - (tmpvar_17 * sign(
      (tmpvar_4.y - tmpvar_12.y)
    )));
    tmpvar_3.y = (tmpvar_12.y - (tmpvar_17 * sign(
      (tmpvar_4.y - tmpvar_12.y)
    )));
  };
  highp vec3 x_18;
  x_18 = (tmpvar_3 - tmpvar_4);
  tmpvar_6.xy = mix (tmpvar_3.xy, tmpvar_4.xy, vTexCoord4.yy);
  tmpvar_7.xy = mix (tmpvar_4.xy, tmpvar_3.xy, vTexCoord4.yy);
  highp vec4 tmpvar_19;
  tmpvar_19.x = (PenThickness.x * vTexCoord4.z);
  tmpvar_19.y = tmpvar_9;
  tmpvar_19.z = tmpvar_10;
  tmpvar_19.w = ((sqrt(
    dot (x_18, x_18)
  ) / PenThickness.y) * abs(vTexCoord4.y));
  tmpvar_8.xzw = tmpvar_19.xzw;
  highp vec4 tmpvar_20;
  tmpvar_20 = mix (vColor1, vColor2, vec4(((1.0 - vTexCoord4.y) * PenThickness.z)));
  tmpvar_8.y = 0.5;
  highp vec4 tmpvar_21;
  tmpvar_21.zw = vec2(0.0, 1.0);
  tmpvar_21.xy = tmpvar_5.xy;
  tmpvar_1 = (tmpvar_21 * tmpvar_2);
  highp vec4 tmpvar_22;
  tmpvar_22 = tmpvar_8;
  highp vec4 tmpvar_23;
  tmpvar_23 = vec4(0.0, 0.0, 0.0, 0.0);
  highp vec4 outPosition_24;
  highp vec4 tmpvar_25;
  tmpvar_25.zw = vec2(0.0, 1.0);
  tmpvar_25.xy = tmpvar_6.xy;
  highp vec4 tmpvar_26;
  tmpvar_26 = (tmpvar_25 * matWorldViewProj);
  outPosition_24 = (tmpvar_26 / abs(tmpvar_26.w));
  highp vec4 tmpvar_27;
  tmpvar_27.zw = vec2(0.0, 1.0);
  tmpvar_27.xy = tmpvar_7.xy;
  highp vec4 tmpvar_28;
  tmpvar_28 = (tmpvar_27 * matWorldViewProj);
  highp vec2 tmpvar_29;
  tmpvar_29 = normalize(((tmpvar_28 / 
    abs(tmpvar_28.w)
  ).xy - outPosition_24.xy));
  outPosition_24.x = (outPosition_24.x - ((tmpvar_19.x * screenSize.z) * tmpvar_29.y));
  outPosition_24.y = (outPosition_24.y + ((tmpvar_19.x * screenSize.w) * tmpvar_29.x));
  if ((PenThickness.x <= 1.0)) {
    tmpvar_22.y = 0.5;
  };
  tmpvar_23 = (tmpvar_20 * PenColor);
  highp vec3 tmpvar_30;
  tmpvar_30.x = tmpvar_22.y;
  tmpvar_30.y = tmpvar_22.w;
  tmpvar_30.z = tmpvar_22.z;
  tmpvar_1.xzw = outPosition_24.xzw;
  tmpvar_1.y = -(outPosition_24.y);
  gl_Position = tmpvar_1;
  xlv_COLOR0 = tmpvar_23;
  xlv_TEXCOORD0 = tmpvar_30;
  xlv_TEXCOORD1 = vec2(0.0, 0.0);
}

