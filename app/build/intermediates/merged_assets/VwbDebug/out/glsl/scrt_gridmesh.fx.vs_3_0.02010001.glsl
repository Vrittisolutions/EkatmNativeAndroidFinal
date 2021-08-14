struct sGridParams {
  highp vec4 ContourScale;
  highp vec4 ContourOffset;
  highp vec4 ContourThickness;
  highp vec4 ContourColorX;
  highp vec4 ContourColorY;
  highp vec4 ContourColorZ;
  highp vec4 Params;
  highp vec4 SurfaceNormal;
  highp vec4 Lighting;
};
uniform highp mat4 matProj;
uniform highp mat4 matWorld;
uniform highp mat4 matWorldView;
uniform highp vec4 PackedFloatParams;
uniform sGridParams GridParams;
uniform highp vec4 TextureDimensionsInv;
uniform highp vec4 MeshCellOfssetScalePosition;
uniform highp vec4 MeshCellOfssetScaleTexCoord;
uniform highp vec4 MeshCellOfssetScaleHeights;
uniform sampler2D HeightMapTexture;
attribute highp vec4 vPosition;
attribute highp vec4 vTexCoord0;
varying highp vec4 xlv_TEXCOORD0;
varying highp vec4 xlv_TEXCOORD1;
varying highp vec3 xlv_TEXCOORD2;
varying highp float xlv_TEXCOORD6;
varying highp vec4 xlv_TEXCOORD7;
void main ()
{
  highp vec4 tmpvar_1;
  highp vec4 tmpvar_2;
  tmpvar_1.w = vPosition.w;
  tmpvar_2.zw = vTexCoord0.zw;
  highp vec4 tmpvar_3;
  highp vec4 tmpvar_4;
  highp vec4 tmpvar_5;
  tmpvar_1.xz = ((vPosition.xz * MeshCellOfssetScalePosition.xy) + MeshCellOfssetScalePosition.zw);
  highp vec2 _texCoord_6;
  _texCoord_6 = ((vTexCoord0.xy * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  _texCoord_6 = mix (TextureDimensionsInv.xy, TextureDimensionsInv.zw, _texCoord_6);
  lowp vec4 tmpvar_7;
  tmpvar_7 = texture2DLod (HeightMapTexture, _texCoord_6, 0.0);
  highp vec4 rgba_8;
  rgba_8 = tmpvar_7.zyxw;
  tmpvar_1.y = (((
    dot (rgba_8.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8))
   * PackedFloatParams.y) + PackedFloatParams.x) * vPosition.w);
  highp vec3 sampledNormal_9;
  highp vec3 tmpvar_10;
  tmpvar_10.z = 0.0;
  tmpvar_10.xy = TextureDimensionsInv.xy;
  highp vec2 _texCoord_11;
  _texCoord_11 = (vTexCoord0.xy - tmpvar_10.xz);
  _texCoord_11 = ((_texCoord_11 * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  lowp vec4 tmpvar_12;
  tmpvar_12 = texture2DLod (HeightMapTexture, _texCoord_11, 0.0);
  highp vec4 rgba_13;
  rgba_13 = tmpvar_12.zyxw;
  highp vec2 _texCoord_14;
  _texCoord_14 = (vTexCoord0.xy + tmpvar_10.xz);
  _texCoord_14 = ((_texCoord_14 * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  lowp vec4 tmpvar_15;
  tmpvar_15 = texture2DLod (HeightMapTexture, _texCoord_14, 0.0);
  highp vec4 rgba_16;
  rgba_16 = tmpvar_15.zyxw;
  highp vec2 _texCoord_17;
  _texCoord_17 = (vTexCoord0.xy - tmpvar_10.zy);
  _texCoord_17 = ((_texCoord_17 * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  lowp vec4 tmpvar_18;
  tmpvar_18 = texture2DLod (HeightMapTexture, _texCoord_17, 0.0);
  highp vec4 rgba_19;
  rgba_19 = tmpvar_18.zyxw;
  highp vec2 _texCoord_20;
  _texCoord_20 = (vTexCoord0.xy + tmpvar_10.zy);
  _texCoord_20 = ((_texCoord_20 * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  lowp vec4 tmpvar_21;
  tmpvar_21 = texture2DLod (HeightMapTexture, _texCoord_20, 0.0);
  highp vec4 rgba_22;
  rgba_22 = tmpvar_21.zyxw;
  sampledNormal_9.x = (((
    ((dot (rgba_13.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y) + PackedFloatParams.x)
   * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y) - ((
    ((dot (rgba_16.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y) + PackedFloatParams.x)
   * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y));
  sampledNormal_9.y = 2.0;
  sampledNormal_9.z = (((
    ((dot (rgba_19.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y) + PackedFloatParams.x)
   * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y) - ((
    ((dot (rgba_22.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y) + PackedFloatParams.x)
   * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y));
  highp vec3 tmpvar_23;
  tmpvar_23 = normalize(sampledNormal_9);
  sampledNormal_9 = tmpvar_23;
  highp mat3 tmpvar_24;
  tmpvar_24[0] = matWorld[0].xyz;
  tmpvar_24[1] = matWorld[1].xyz;
  tmpvar_24[2] = matWorld[2].xyz;
  highp vec2 _texCoord_25;
  _texCoord_25 = ((vTexCoord0.zw * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  _texCoord_25 = mix (TextureDimensionsInv.xy, TextureDimensionsInv.zw, _texCoord_25);
  lowp vec4 tmpvar_26;
  tmpvar_26 = texture2DLod (HeightMapTexture, _texCoord_25, 0.0);
  highp vec4 rgba_27;
  rgba_27 = tmpvar_26.zyxw;
  highp float tmpvar_28;
  tmpvar_28 = GridParams.Params.w;
  highp float tmpvar_29;
  tmpvar_29 = mix (tmpvar_1.y, ((
    (dot (rgba_27.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y)
   + PackedFloatParams.x) * vPosition.w), tmpvar_28);
  tmpvar_2.xy = mix (vTexCoord0.xy, vTexCoord0.zw, vec2(tmpvar_28));
  tmpvar_5 = tmpvar_2;
  tmpvar_2.xy = clamp (tmpvar_2.xy, 0.0, 1.0);
  tmpvar_2.xy = mix (TextureDimensionsInv.xy, TextureDimensionsInv.zw, tmpvar_2.xy);
  tmpvar_2.xy = ((tmpvar_2.xy * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  tmpvar_1.y = ((tmpvar_1.y * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y);
  tmpvar_4 = tmpvar_1;
  tmpvar_1.xz = tmpvar_1.xz;
  tmpvar_1.w = 1.0;
  tmpvar_3.xy = tmpvar_2.xy;
  tmpvar_3.zw = tmpvar_2.zw;
  gl_Position = (tmpvar_1 * (matWorldView * matProj));
  xlv_TEXCOORD0 = tmpvar_3;
  xlv_TEXCOORD1 = tmpvar_4;
  xlv_TEXCOORD2 = (tmpvar_23 * tmpvar_24);
  xlv_TEXCOORD6 = tmpvar_29;
  xlv_TEXCOORD7 = tmpvar_5;
}

